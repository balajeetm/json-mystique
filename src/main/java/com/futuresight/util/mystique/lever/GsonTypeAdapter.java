package com.futuresight.util.mystique.lever;

import java.lang.reflect.Type;

import lombok.Data;

@Data
public class GsonTypeAdapter {

	private Type type;

	private Object adapter;

}
