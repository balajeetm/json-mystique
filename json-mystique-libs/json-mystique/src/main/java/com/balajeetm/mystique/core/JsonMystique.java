/*
 * Copyright (c) Balajee TM 2016.
 * All rights reserved.
 * License -  @see <a href="http://www.apache.org/licenses/LICENSE-2.0"></a>
 */

/*
 * Created on 25 Aug, 2016 by balajeetm
 * http://www.balajeetm.com
 */
package com.balajeetm.mystique.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.balajeetm.mystique.core.lever.MystiqueLever;
import com.balajeetm.mystique.core.util.MystiqueConstants;
import com.balajeetm.mystique.util.gson.convertor.GsonConvertor;
import com.balajeetm.mystique.util.json.convertor.JsonConvertor;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class JsonMystique.
 *
 * @author balajeetm
 */

/** The Constant log. */
@Slf4j
public class JsonMystique {

  /** The factory. */
  private MystiqueFactory factory;

  /** The json lever. */
  private MystiqueLever jsonLever;

  /** The gson convertor. */
  private JsonConvertor gsonConvertor;

  /** The mystiques. */
  private Map<String, Map<String, List<Tarot>>> mystiques;

  /** Instantiates a new json genie. */
  private JsonMystique() {
    mystiques = new HashMap<>();
    gsonConvertor = GsonConvertor.getInstance();
    jsonLever = MystiqueLever.getInstance();
    factory = MystiqueFactory.getInstance();
    init();
  }

  /**
   * Gets the single instance of JsonMystique.
   *
   * @return single instance of JsonMystique
   */
  public static JsonMystique getInstance() {
    return Creator.INSTANCE;
  }

  // Efficient Thread safe Lazy Initialization
  // works only if the singleton constructor is non parameterized
  /** The Class Creator. */
  private static class Creator {

    /** The instance. */
    private static JsonMystique INSTANCE = new JsonMystique();
  }

  /** Inits the. */
  protected void init() {
    /* get all mystique rule files in the classpath */
    String locationPattern = "classpath*:jsonmystique/**/*.mys";
    ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    Resource[] resources = null;
    try {
      resources = resourceResolver.getResources(locationPattern);
    } catch (IOException e) {
      log.info(
          String.format("Error loading mystiques from %s - %s", locationPattern, e.getMessage()),
          e);
      return;
    }

    if (!ArrayUtils.isEmpty(resources)) {
      for (Resource resource : resources) {
        if (resource.exists()) {
          String specName = FilenameUtils.removeExtension(resource.getFilename());
          try {
            List<Tarot> tarotList =
                gsonConvertor.deserializeList(resource.getInputStream(), Tarot.class);
            Map<String, List<Tarot>> tarotMap = new HashMap<>();
            List<Tarot> depsList = new ArrayList<>();
            List<Tarot> ruleList = new ArrayList<>();
            tarotMap.put(MystiqueConstants.DEPS, depsList);
            tarotMap.put(MystiqueConstants.TAROTS, ruleList);
            mystiques.put(specName, tarotMap);
            for (Tarot tarot : tarotList) {
              List<Tarot> deps = tarot.getDeps();
              if (CollectionUtils.isNotEmpty(deps)) {
                depsList.addAll(deps);
              }
              tarot.setDeps(null);
              ruleList.add(tarot);
            }

          } catch (JsonSyntaxException | IllegalArgumentException | IOException exception) {
            log.info(
                String.format(
                    "Unable to load mystiques %s from %s - %s. Trying to load other mystiques if any",
                    specName, locationPattern, exception.getMessage()),
                exception);
            continue;
          }
        }
      }
    } else {
      log.warn(String.format("No mystiques found @ %s for transformation", locationPattern));
    }
  }

  /**
   * Transform.
   *
   * @param inputJson the input json
   * @param specName the spec name
   * @return the json element
   */
  public JsonElement transform(String inputJson, String specName) {
    JsonElement source = jsonLever.getJsonParser().parse(inputJson);
    return transform(source, specName);
  }

  /**
   * Transform.
   *
   * @param source the source
   * @param specName the spec name
   * @return the json element
   */
  public JsonElement transform(JsonElement source, String specName) {
    return transform(source, specName, new JsonObject());
  }

  /**
   * Transform.
   *
   * @param inputJson the input json
   * @param specName the spec name
   * @param deps the deps
   * @return the json element
   */
  public JsonElement transform(String inputJson, String specName, JsonObject deps) {
    JsonElement source = jsonLever.getJsonParser().parse(inputJson);
    return transform(source, specName, deps);
  }

  /**
   * Transform.
   *
   * @param source the source
   * @param specName the spec name
   * @param deps the deps
   * @return the json element
   */
  public JsonElement transform(JsonElement source, String specName, JsonObject deps) {
    return transform(source, specName, deps, null);
  }

  /**
   * Transform.
   *
   * @param source the source
   * @param specName the spec name
   * @param deps the deps
   * @param aces the aces
   * @return the json element
   */
  public JsonElement transform(
      JsonElement source, String specName, JsonObject deps, JsonObject aces) {
    JsonElement transform = JsonNull.INSTANCE;
    Map<String, List<Tarot>> map = mystiques.get(specName);
    deps = jsonLever.asJsonObject(deps, new JsonObject());
    if (null != map) {
      List<Tarot> depList = map.get(MystiqueConstants.DEPS);
      updateDependencies(source, depList, deps);
      List<Tarot> tarotList = map.get(MystiqueConstants.TAROTS);
      transform = transform(source, tarotList, deps, aces);
    }

    if (jsonLever.isNull(transform)) {
      log.info(String.format("Transformed value for spec %s is null", specName));
    }
    return transform;
  }

  /**
   * Transform to string.
   *
   * @param inputJson the input json
   * @param specName the spec name
   * @return the string
   */
  public String transformToString(String inputJson, String specName) {
    return String.valueOf(transform(inputJson, specName));
  }

  /**
   * Transform to string.
   *
   * @param inputJson the input json
   * @param specName the spec name
   * @param deps the deps
   * @return the string
   */
  public String transformToString(String inputJson, String specName, JsonObject deps) {
    return String.valueOf(transform(inputJson, specName, deps));
  }

  /**
   * Transform to string.
   *
   * @param source the source
   * @param specName the spec name
   * @return the string
   */
  public String transformToString(JsonElement source, String specName) {
    return String.valueOf(transform(source, specName));
  }

  /**
   * Transform to string.
   *
   * @param source the source
   * @param specName the spec name
   * @param deps the deps
   * @return the string
   */
  public String transformToString(JsonElement source, String specName, JsonObject deps) {
    return String.valueOf(transform(source, specName, deps));
  }

  /**
   * Transform.
   *
   * @param source the source
   * @param tarotList the tarot list
   * @param dependencies the dependencies
   * @return the json element
   */
  private JsonElement transform(
      JsonElement source, List<Tarot> tarotList, JsonObject dependencies) {
    return transform(source, tarotList, dependencies, null);
  }

  /**
   * Transform.
   *
   * @param source the source
   * @param tarotList the tarot list
   * @param dependencies the dependencies
   * @param parentAces the parent aces
   * @return the json element
   */
  private JsonElement transform(
      JsonElement source, List<Tarot> tarotList, JsonObject dependencies, JsonObject parentAces) {
    JsonObject resultWrapper = new JsonObject();
    resultWrapper.add(MystiqueConstants.RESULT, JsonNull.INSTANCE);
    if (CollectionUtils.isNotEmpty(tarotList)) {
      List<CompletableFuture<JsonObject>> cfs = new ArrayList<>();
      for (Tarot tarot : tarotList) {
        JsonObject turn = tarot.getTurn();

        CompletableFuture<JsonObject> getAces =
            CompletableFuture.supplyAsync(
                    () -> {
                      JsonObject aces = tarot.getAces();
                      JsonObject updatedAces =
                          jsonLever.getUpdatedAces(
                              source, aces, dependencies, jsonLever.deepClone(aces));
                      jsonLever.simpleMerge(updatedAces, parentAces);
                      return updatedAces;
                    })
                .exceptionally(
                    e -> {
                      String msg =
                          String.format(
                              "Error updating aces for turn %s - %s", turn, e.getMessage());
                      log.info(msg, e);
                      return parentAces;
                    });

        CompletableFuture<JsonElement> transformAsync =
            getAces
                .thenApplyAsync(
                    (aces) -> {
                      JsonElement transform = JsonNull.INSTANCE;
                      Spell spell =
                          getSpell(
                              source,
                              jsonLever.getJPath(tarot.getFrom()),
                              dependencies,
                              aces,
                              turn,
                              resultWrapper);
                      MystTurn mystique = factory.getMystTurn(turn);
                      transform = spell.cast(mystique);
                      return transform;
                    })
                .exceptionally(
                    (e) -> {
                      String msg =
                          String.format(
                              "Error transforming input with specification for turn %s - %s",
                              turn, e.getMessage());
                      log.info(msg, e);
                      return JsonNull.INSTANCE;
                    });

        CompletableFuture<JsonObject> setResult =
            getAces
                .thenCombine(
                    transformAsync,
                    (aces, transform) ->
                        jsonLever.set(
                            resultWrapper,
                            jsonLever.getJPath(tarot.getTo()),
                            transform,
                            aces,
                            tarot.getOptional()))
                .exceptionally(
                    e -> {
                      String msg =
                          String.format(
                              "Error setting output for turn %s - %s", turn, e.getMessage());
                      log.info(msg, e);
                      return resultWrapper;
                    });

        cfs.add(setResult);
      }

      for (CompletableFuture<JsonObject> completableFuture : cfs) {
        completableFuture.join();
      }
    } else {
      log.info(String.format("Invalid tarots. Tarots cannot be empty"));
    }
    return resultWrapper.get(MystiqueConstants.RESULT);
  }

  /**
   * Gets the spell.
   *
   * @param source the source
   * @param from the from
   * @param dependencies the dependencies
   * @param aces the aces
   * @param turn the turn
   * @param resultWrapper the result wrapper
   * @return the spell
   */
  private Spell getSpell(
      JsonElement source,
      JsonArray from,
      JsonObject dependencies,
      JsonObject aces,
      JsonObject turn,
      JsonObject resultWrapper) {
    List<JsonElement> fields = new ArrayList<>();
    Boolean isFromLoopy = getFields(source, dependencies, aces, from, fields);
    // Ideally isDeps should never be loopy
    Spell spell =
        isFromLoopy
            ? new LoopySpell(fields, dependencies, aces, turn, resultWrapper)
            : new SimpleSpell(fields, dependencies, aces, turn, resultWrapper);
    return spell;
  }

  /**
   * Gets the fields.
   *
   * @param source the source
   * @param dependencies the dependencies
   * @param aces the aces
   * @param path the path
   * @param fields the fields
   * @return the fields
   */
  private Boolean getFields(
      JsonElement source,
      JsonObject dependencies,
      JsonObject aces,
      JsonArray path,
      List<JsonElement> fields) {
    Boolean isLoopy = Boolean.FALSE;
    if (null != path) {
      if (path.size() > 0) {
        for (JsonElement jsonElement : path) {
          if (jsonElement.isJsonArray()) {
            JsonArray fromArray = jsonElement.getAsJsonArray();
            isLoopy =
                isLoopy || jsonLever.updateFields(source, dependencies, aces, fields, fromArray);
            // Once isloopy, the loop doesn't execute anymore
          } else {
            isLoopy = isLoopy || jsonLever.updateFields(source, dependencies, aces, fields, path);
            break;
          }
        }
      } else {
        isLoopy = isLoopy || jsonLever.updateFields(source, dependencies, aces, fields, path);
      }
    }
    return isLoopy;
  }

  /**
   * Update dependencies.
   *
   * @param source the source
   * @param deps the deps
   * @param dependencies the dependencies
   */
  private void updateDependencies(JsonElement source, List<Tarot> deps, JsonObject dependencies) {
    if (CollectionUtils.isNotEmpty(deps)) {
      try {
        JsonObject transformJson =
            jsonLever.asJsonObject(transform(source, deps, dependencies), new JsonObject());
        jsonLever.simpleMerge(dependencies, transformJson);
      } catch (RuntimeException e) {
        log.info(String.format("Could not update dependencies : %s", e.getMessage()));
      }
    }
  }

  /**
   * Register a custom turn.
   *
   * @param turn the turn
   */
  public void register(MystTurn turn) {
    factory.register(turn);
  }
}
