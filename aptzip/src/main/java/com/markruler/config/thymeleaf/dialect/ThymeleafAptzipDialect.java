package com.markruler.config.thymeleaf.dialect;

import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class ThymeleafAptzipDialect extends AbstractDialect implements IExpressionObjectDialect {

  private final IExpressionObjectFactory APTZIP_EXPRESSION_OBJECTS_FACTORY = new ThymeleafAptzipExpressionFactory();

  public ThymeleafAptzipDialect() {
      super("aptzip");
  }

  @Override
  public IExpressionObjectFactory getExpressionObjectFactory() {
      return APTZIP_EXPRESSION_OBJECTS_FACTORY;
  }

}