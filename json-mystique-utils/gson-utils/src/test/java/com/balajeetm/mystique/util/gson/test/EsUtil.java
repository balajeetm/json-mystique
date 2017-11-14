package com.balajeetm.mystique.util.gson.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;

import com.balajeetm.mystique.util.gson.lever.JsonLever;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class EsUtil {

  Random RANDOM = new Random();
  DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

  public enum State {
    Open,
    Committed,
    CheckedOut,
    Closed;

    public static List<State> list() {
      return Collections.unmodifiableList(Arrays.asList(values()));
    }
  }

  public enum Channel {
    ghs,
    gmo,
    store;

    public static List<Channel> list() {
      return Collections.unmodifiableList(Arrays.asList(values()));
    }

    public static Channel random() {
      return Channel.values()[ThreadLocalRandom.current().nextInt(0, Channel.values().length)];
    }
  }

  public enum Product {
    DoveShampoo("dove-s", "Dove Shampoo"),
    DoveConditioner("dove-c", "Dove Conditioner"),
    LuxShampoo("lux-s", "Lux Shampoo"),
    LuxConditioner("Lux-c", "Lux Conditioner"),
    Fruit_Mango("mango", "Mango"),
    Fruit_Orange("orange", "Orange"),
    Wires("wires", "Conduit Wires"),
    Tubes("tubes", "Shallow Tubes"),
    TitanWatch("twatch", "Titan Watch"),
    RolexWatch("rwatch", "Rolex Watch"),
    LevisWallet("lwallet", "Levis Wallet"),
    ;

    private String id;

    private String name;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    private Product(String id, String name) {
      this.id = id;
      this.name = name;
    }

    public static List<Product> list() {
      return Collections.unmodifiableList(Arrays.asList(values()));
    }

    public static Product random() {
      return Product.values()[ThreadLocalRandom.current().nextInt(0, Product.values().length)];
    }

    public static Product random(Set<Product> notin) {

      Product ran = null;
      if (CollectionUtils.size(notin) < Product.values().length) {
        Boolean invalid = Boolean.TRUE;
        while (invalid) {
          ran = random();
          invalid = notin.contains(ran);
        }
        notin.add(ran);
      }
      return ran;
    }
  }

  private JsonArray createRandomOrders(Integer count) {
    JsonLever lever = JsonLever.getInstance();
    if (count == null || count < 1) {
      count = 10;
    }

    List<State> list = State.list();
    List<Channel> chList = Channel.list();
    JsonArray array = new JsonArray();

    LocalDateTime end = LocalDateTime.now();
    LocalDateTime start = end.minusMonths(8);

    for (int i = 0; i < count; i++) {
      JsonObject order = new JsonObject();
      Long dateLong =
          ThreadLocalRandom.current()
              .nextLong(start.toEpochSecond(ZoneOffset.UTC), end.toEpochSecond(ZoneOffset.UTC));
      String orderID =
          String.format("trn:tesco:order:%suk:%s", Channel.random().name(), UUID.randomUUID());

      JsonArray orderLines = new JsonArray();
      Integer lc = ThreadLocalRandom.current().nextInt(5);
      Set<Product> pro = new HashSet<>();
      for (int k = 0; k < lc; k++) {
        JsonObject line = new JsonObject();
        Product ranpro = Product.random(pro);
        lever.set(line, "productId", new JsonPrimitive(ranpro.getId()));
        lever.set(line, "productDescription", new JsonPrimitive(ranpro.getName()));
        orderLines.add(line);
      }

      order.addProperty("orderId", orderID);
      order.addProperty(
          "customerNumber",
          String.format(
              "trn:tesco:uid:uuid:customerId-%s", ThreadLocalRandom.current().nextInt(5)));
      order.addProperty("orderState", list.get(RANDOM.nextInt(list.size())).name());
      order.addProperty("channel", chList.get(RANDOM.nextInt(chList.size())).name());
      order.addProperty(
          "orderDate", formatter.format(LocalDateTime.ofEpochSecond(dateLong, 0, ZoneOffset.UTC)));
      order.add("orderLines", orderLines);
      array.add(order);
    }

    return array;
  }

  private void createBulkFile(JsonArray orders, String file) throws IOException {
    File files = new File(file);
    FileUtils.writeStringToFile(files, "", Charset.defaultCharset());
    for (JsonElement ele : orders) {
      JsonObject eleJson = ele.getAsJsonObject();
      JsonObject index = new JsonObject();
      JsonObject indexJson = new JsonObject();
      indexJson.add("_id", eleJson.get("orderId"));
      indexJson.add("_routing", eleJson.get("customerNumber"));
      index.add("index", indexJson);
      FileUtils.writeStringToFile(files, index.toString(), Charset.defaultCharset(), true);
      FileUtils.writeStringToFile(files, "\n", Charset.defaultCharset(), true);
      JsonObject order = new JsonObject();
      JsonLever instance = JsonLever.getInstance();
      instance.set(order, "doc.orderId", eleJson.get("orderId"));
      instance.set(
          order,
          instance.newJsonArray("doc", "extendedOrder", "orderState"),
          eleJson.get("orderState"));
      instance.set(
          order, instance.newJsonArray("doc", "extendedOrder", "channel"), eleJson.get("channel"));
      instance.set(
          order,
          instance.newJsonArray("doc", "extendedOrder", "orderDate"),
          eleJson.get("orderDate"));
      instance.set(
          order,
          instance.newJsonArray("doc", "orderDetails", "customerNumber"),
          eleJson.get("customerNumber"));
      instance.set(order, "doc.orderLines", eleJson.get("orderLines"));
      FileUtils.writeStringToFile(files, order.toString(), Charset.defaultCharset(), true);
      FileUtils.writeStringToFile(files, "\n", Charset.defaultCharset(), true);
    }
  }

  public static void main(String[] args) {
    try {
      EsUtil es = new EsUtil();
      JsonArray createRandomOrders = es.createRandomOrders(100);
      System.out.println(createRandomOrders.size());
      es.createBulkFile(createRandomOrders, "es/bulk/gen.json");
      System.out.println("O");
    } catch (Exception e) {
      System.err.println("E");
    }
  }
}
