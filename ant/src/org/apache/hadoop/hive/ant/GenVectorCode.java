/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hadoop.hive.ant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * This class generates java classes from the templates.
 */
public class GenVectorCode extends Task {

  private static String [][] templateExpansions =
    {
      {"ColumnArithmeticScalar", "Add", "long", "long", "+"},
      {"ColumnArithmeticScalar", "Subtract", "long", "long", "-"},
      {"ColumnArithmeticScalar", "Multiply", "long", "long", "*"},
      {"ColumnArithmeticScalar", "Modulo", "long", "long", "%"},

      {"ColumnArithmeticScalar", "Add", "long", "double", "+"},
      {"ColumnArithmeticScalar", "Subtract", "long", "double", "-"},
      {"ColumnArithmeticScalar", "Multiply", "long", "double", "*"},
      {"ColumnArithmeticScalar", "Divide", "long", "double", "/"},
      {"ColumnArithmeticScalar", "Modulo", "long", "double", "%"},

      {"ColumnArithmeticScalar", "Add", "double", "long", "+"},
      {"ColumnArithmeticScalar", "Subtract", "double", "long", "-"},
      {"ColumnArithmeticScalar", "Multiply", "double", "long", "*"},
      {"ColumnArithmeticScalar", "Divide", "double", "long", "/"},
      {"ColumnArithmeticScalar", "Modulo", "double", "long", "%"},

      {"ColumnArithmeticScalar", "Add", "double", "double", "+"},
      {"ColumnArithmeticScalar", "Subtract", "double", "double", "-"},
      {"ColumnArithmeticScalar", "Multiply", "double", "double", "*"},
      {"ColumnArithmeticScalar", "Divide", "double", "double", "/"},
      {"ColumnArithmeticScalar", "Modulo", "double", "double", "%"},

      {"ScalarArithmeticColumn", "Add", "long", "long", "+"},
      {"ScalarArithmeticColumn", "Subtract", "long", "long", "-"},
      {"ScalarArithmeticColumn", "Multiply", "long", "long", "*"},
      {"ScalarArithmeticColumn", "Modulo", "long", "long", "%"},

      {"ScalarArithmeticColumn", "Add", "long", "double", "+"},
      {"ScalarArithmeticColumn", "Subtract", "long", "double", "-"},
      {"ScalarArithmeticColumn", "Multiply", "long", "double", "*"},
      {"ScalarArithmeticColumn", "Divide", "long", "double", "/"},
      {"ScalarArithmeticColumn", "Modulo", "long", "double", "%"},

      {"ScalarArithmeticColumn", "Add", "double", "long", "+"},
      {"ScalarArithmeticColumn", "Subtract", "double", "long", "-"},
      {"ScalarArithmeticColumn", "Multiply", "double", "long", "*"},
      {"ScalarArithmeticColumn", "Divide", "double", "long", "/"},
      {"ScalarArithmeticColumn", "Modulo", "double", "long", "%"},

      {"ScalarArithmeticColumn", "Add", "double", "double", "+"},
      {"ScalarArithmeticColumn", "Subtract", "double", "double", "-"},
      {"ScalarArithmeticColumn", "Multiply", "double", "double", "*"},
      {"ScalarArithmeticColumn", "Divide", "double", "double", "/"},
      {"ScalarArithmeticColumn", "Modulo", "double", "double", "%"},

      {"ColumnArithmeticColumn", "Add", "long", "long", "+"},
      {"ColumnArithmeticColumn", "Subtract", "long", "long", "-"},
      {"ColumnArithmeticColumn", "Multiply", "long", "long", "*"},
      {"ColumnArithmeticColumn", "Modulo", "long", "long", "%"},

      {"ColumnArithmeticColumn", "Add", "long", "double", "+"},
      {"ColumnArithmeticColumn", "Subtract", "long", "double", "-"},
      {"ColumnArithmeticColumn", "Multiply", "long", "double", "*"},
      {"ColumnArithmeticColumn", "Divide", "long", "double", "/"},
      {"ColumnArithmeticColumn", "Modulo", "long", "double", "%"},

      {"ColumnArithmeticColumn", "Add", "double", "long", "+"},
      {"ColumnArithmeticColumn", "Subtract", "double", "long", "-"},
      {"ColumnArithmeticColumn", "Multiply", "double", "long", "*"},
      {"ColumnArithmeticColumn", "Divide", "double", "long", "/"},
      {"ColumnArithmeticColumn", "Modulo", "double", "long", "%"},

      {"ColumnArithmeticColumn", "Add", "double", "double", "+"},
      {"ColumnArithmeticColumn", "Subtract", "double", "double", "-"},
      {"ColumnArithmeticColumn", "Multiply", "double", "double", "*"},
      {"ColumnArithmeticColumn", "Divide", "double", "double", "/"},
      {"ColumnArithmeticColumn", "Modulo", "double", "double", "%"},

      {"ColumnCompareScalar", "Equal", "long", "double", "=="},
      {"ColumnCompareScalar", "Equal", "double", "double", "=="},
      {"ColumnCompareScalar", "NotEqual", "long", "double", "!="},
      {"ColumnCompareScalar", "NotEqual", "double", "double", "!="},
      {"ColumnCompareScalar", "Less", "long", "double", "<"},
      {"ColumnCompareScalar", "Less", "double", "double", "<"},
      {"ColumnCompareScalar", "LessEqual", "long", "double", "<="},
      {"ColumnCompareScalar", "LessEqual", "double", "double", "<="},
      {"ColumnCompareScalar", "Greater", "long", "double", ">"},
      {"ColumnCompareScalar", "Greater", "double", "double", ">"},
      {"ColumnCompareScalar", "GreaterEqual", "long", "double", ">="},
      {"ColumnCompareScalar", "GreaterEqual", "double", "double", ">="},

      {"ColumnCompareScalar", "Equal", "long", "long", "=="},
      {"ColumnCompareScalar", "Equal", "double", "long", "=="},
      {"ColumnCompareScalar", "NotEqual", "long", "long", "!="},
      {"ColumnCompareScalar", "NotEqual", "double", "long", "!="},
      {"ColumnCompareScalar", "Less", "long", "long", "<"},
      {"ColumnCompareScalar", "Less", "double", "long", "<"},
      {"ColumnCompareScalar", "LessEqual", "long", "long", "<="},
      {"ColumnCompareScalar", "LessEqual", "double", "long", "<="},
      {"ColumnCompareScalar", "Greater", "long", "long", ">"},
      {"ColumnCompareScalar", "Greater", "double", "long", ">"},
      {"ColumnCompareScalar", "GreaterEqual", "long", "long", ">="},
      {"ColumnCompareScalar", "GreaterEqual", "double", "long", ">="},

      {"ScalarCompareColumn", "Equal", "long", "double", "=="},
      {"ScalarCompareColumn", "Equal", "double", "double", "=="},
      {"ScalarCompareColumn", "NotEqual", "long", "double", "!="},
      {"ScalarCompareColumn", "NotEqual", "double", "double", "!="},
      {"ScalarCompareColumn", "Less", "long", "double", "<"},
      {"ScalarCompareColumn", "Less", "double", "double", "<"},
      {"ScalarCompareColumn", "LessEqual", "long", "double", "<="},
      {"ScalarCompareColumn", "LessEqual", "double", "double", "<="},
      {"ScalarCompareColumn", "Greater", "long", "double", ">"},
      {"ScalarCompareColumn", "Greater", "double", "double", ">"},
      {"ScalarCompareColumn", "GreaterEqual", "long", "double", ">="},
      {"ScalarCompareColumn", "GreaterEqual", "double", "double", ">="},

      {"ScalarCompareColumn", "Equal", "long", "long", "=="},
      {"ScalarCompareColumn", "Equal", "double", "long", "=="},
      {"ScalarCompareColumn", "NotEqual", "long", "long", "!="},
      {"ScalarCompareColumn", "NotEqual", "double", "long", "!="},
      {"ScalarCompareColumn", "Less", "long", "long", "<"},
      {"ScalarCompareColumn", "Less", "double", "long", "<"},
      {"ScalarCompareColumn", "LessEqual", "long", "long", "<="},
      {"ScalarCompareColumn", "LessEqual", "double", "long", "<="},
      {"ScalarCompareColumn", "Greater", "long", "long", ">"},
      {"ScalarCompareColumn", "Greater", "double", "long", ">"},
      {"ScalarCompareColumn", "GreaterEqual", "long", "long", ">="},
      {"ScalarCompareColumn", "GreaterEqual", "double", "long", ">="},

      {"FilterColumnCompareScalar", "Equal", "long", "double", "=="},
      {"FilterColumnCompareScalar", "Equal", "double", "double", "=="},
      {"FilterColumnCompareScalar", "NotEqual", "long", "double", "!="},
      {"FilterColumnCompareScalar", "NotEqual", "double", "double", "!="},
      {"FilterColumnCompareScalar", "Less", "long", "double", "<"},
      {"FilterColumnCompareScalar", "Less", "double", "double", "<"},
      {"FilterColumnCompareScalar", "LessEqual", "long", "double", "<="},
      {"FilterColumnCompareScalar", "LessEqual", "double", "double", "<="},
      {"FilterColumnCompareScalar", "Greater", "long", "double", ">"},
      {"FilterColumnCompareScalar", "Greater", "double", "double", ">"},
      {"FilterColumnCompareScalar", "GreaterEqual", "long", "double", ">="},
      {"FilterColumnCompareScalar", "GreaterEqual", "double", "double", ">="},

      {"FilterColumnCompareScalar", "Equal", "long", "long", "=="},
      {"FilterColumnCompareScalar", "Equal", "double", "long", "=="},
      {"FilterColumnCompareScalar", "NotEqual", "long", "long", "!="},
      {"FilterColumnCompareScalar", "NotEqual", "double", "long", "!="},
      {"FilterColumnCompareScalar", "Less", "long", "long", "<"},
      {"FilterColumnCompareScalar", "Less", "double", "long", "<"},
      {"FilterColumnCompareScalar", "LessEqual", "long", "long", "<="},
      {"FilterColumnCompareScalar", "LessEqual", "double", "long", "<="},
      {"FilterColumnCompareScalar", "Greater", "long", "long", ">"},
      {"FilterColumnCompareScalar", "Greater", "double", "long", ">"},
      {"FilterColumnCompareScalar", "GreaterEqual", "long", "long", ">="},
      {"FilterColumnCompareScalar", "GreaterEqual", "double", "long", ">="},

      {"FilterScalarCompareColumn", "Equal", "long", "double", "=="},
      {"FilterScalarCompareColumn", "Equal", "double", "double", "=="},
      {"FilterScalarCompareColumn", "NotEqual", "long", "double", "!="},
      {"FilterScalarCompareColumn", "NotEqual", "double", "double", "!="},
      {"FilterScalarCompareColumn", "Less", "long", "double", "<"},
      {"FilterScalarCompareColumn", "Less", "double", "double", "<"},
      {"FilterScalarCompareColumn", "LessEqual", "long", "double", "<="},
      {"FilterScalarCompareColumn", "LessEqual", "double", "double", "<="},
      {"FilterScalarCompareColumn", "Greater", "long", "double", ">"},
      {"FilterScalarCompareColumn", "Greater", "double", "double", ">"},
      {"FilterScalarCompareColumn", "GreaterEqual", "long", "double", ">="},
      {"FilterScalarCompareColumn", "GreaterEqual", "double", "double", ">="},

      {"FilterScalarCompareColumn", "Equal", "long", "long", "=="},
      {"FilterScalarCompareColumn", "Equal", "double", "long", "=="},
      {"FilterScalarCompareColumn", "NotEqual", "long", "long", "!="},
      {"FilterScalarCompareColumn", "NotEqual", "double", "long", "!="},
      {"FilterScalarCompareColumn", "Less", "long", "long", "<"},
      {"FilterScalarCompareColumn", "Less", "double", "long", "<"},
      {"FilterScalarCompareColumn", "LessEqual", "long", "long", "<="},
      {"FilterScalarCompareColumn", "LessEqual", "double", "long", "<="},
      {"FilterScalarCompareColumn", "Greater", "long", "long", ">"},
      {"FilterScalarCompareColumn", "Greater", "double", "long", ">"},
      {"FilterScalarCompareColumn", "GreaterEqual", "long", "long", ">="},
      {"FilterScalarCompareColumn", "GreaterEqual", "double", "long", ">="},

      {"FilterStringColumnCompareScalar", "Equal", "=="},
      {"FilterStringColumnCompareScalar", "NotEqual", "!="},
      {"FilterStringColumnCompareScalar", "Less", "<"},
      {"FilterStringColumnCompareScalar", "LessEqual", "<="},
      {"FilterStringColumnCompareScalar", "Greater", ">"},
      {"FilterStringColumnCompareScalar", "GreaterEqual", ">="},

      {"StringColumnCompareScalar", "Equal", "=="},
      {"StringColumnCompareScalar", "NotEqual", "!="},
      {"StringColumnCompareScalar", "Less", "<"},
      {"StringColumnCompareScalar", "LessEqual", "<="},
      {"StringColumnCompareScalar", "Greater", ">"},
      {"StringColumnCompareScalar", "GreaterEqual", ">="},

      {"FilterStringScalarCompareColumn", "Equal", "=="},
      {"FilterStringScalarCompareColumn", "NotEqual", "!="},
      {"FilterStringScalarCompareColumn", "Less", "<"},
      {"FilterStringScalarCompareColumn", "LessEqual", "<="},
      {"FilterStringScalarCompareColumn", "Greater", ">"},
      {"FilterStringScalarCompareColumn", "GreaterEqual", ">="},

      {"StringScalarCompareColumn", "Equal", "=="},
      {"StringScalarCompareColumn", "NotEqual", "!="},
      {"StringScalarCompareColumn", "Less", "<"},
      {"StringScalarCompareColumn", "LessEqual", "<="},
      {"StringScalarCompareColumn", "Greater", ">"},
      {"StringScalarCompareColumn", "GreaterEqual", ">="},

      {"FilterStringColumnCompareColumn", "Equal", "=="},
      {"FilterStringColumnCompareColumn", "NotEqual", "!="},
      {"FilterStringColumnCompareColumn", "Less", "<"},
      {"FilterStringColumnCompareColumn", "LessEqual", "<="},
      {"FilterStringColumnCompareColumn", "Greater", ">"},
      {"FilterStringColumnCompareColumn", "GreaterEqual", ">="},

      {"StringColumnCompareColumn", "Equal", "=="},
      {"StringColumnCompareColumn", "NotEqual", "!="},
      {"StringColumnCompareColumn", "Less", "<"},
      {"StringColumnCompareColumn", "LessEqual", "<="},
      {"StringColumnCompareColumn", "Greater", ">"},
      {"StringColumnCompareColumn", "GreaterEqual", ">="},

      {"FilterColumnCompareColumn", "Equal", "long", "double", "=="},
      {"FilterColumnCompareColumn", "Equal", "double", "double", "=="},
      {"FilterColumnCompareColumn", "NotEqual", "long", "double", "!="},
      {"FilterColumnCompareColumn", "NotEqual", "double", "double", "!="},
      {"FilterColumnCompareColumn", "Less", "long", "double", "<"},
      {"FilterColumnCompareColumn", "Less", "double", "double", "<"},
      {"FilterColumnCompareColumn", "LessEqual", "long", "double", "<="},
      {"FilterColumnCompareColumn", "LessEqual", "double", "double", "<="},
      {"FilterColumnCompareColumn", "Greater", "long", "double", ">"},
      {"FilterColumnCompareColumn", "Greater", "double", "double", ">"},
      {"FilterColumnCompareColumn", "GreaterEqual", "long", "double", ">="},
      {"FilterColumnCompareColumn", "GreaterEqual", "double", "double", ">="},

        {"FilterColumnCompareColumn", "Equal", "long", "long", "=="},
        {"FilterColumnCompareColumn", "Equal", "double", "long", "=="},
        {"FilterColumnCompareColumn", "NotEqual", "long", "long", "!="},
        {"FilterColumnCompareColumn", "NotEqual", "double", "long", "!="},
        {"FilterColumnCompareColumn", "Less", "long", "long", "<"},
        {"FilterColumnCompareColumn", "Less", "double", "long", "<"},
        {"FilterColumnCompareColumn", "LessEqual", "long", "long", "<="},
        {"FilterColumnCompareColumn", "LessEqual", "double", "long", "<="},
        {"FilterColumnCompareColumn", "Greater", "long", "long", ">"},
        {"FilterColumnCompareColumn", "Greater", "double", "long", ">"},
        {"FilterColumnCompareColumn", "GreaterEqual", "long", "long", ">="},
        {"FilterColumnCompareColumn", "GreaterEqual", "double", "long", ">="},

      {"ColumnCompareColumn", "Equal", "long", "double", "=="},
      {"ColumnCompareColumn", "Equal", "double", "double", "=="},
      {"ColumnCompareColumn", "NotEqual", "long", "double", "!="},
      {"ColumnCompareColumn", "NotEqual", "double", "double", "!="},
      {"ColumnCompareColumn", "Less", "long", "double", "<"},
      {"ColumnCompareColumn", "Less", "double", "double", "<"},
      {"ColumnCompareColumn", "LessEqual", "long", "double", "<="},
      {"ColumnCompareColumn", "LessEqual", "double", "double", "<="},
      {"ColumnCompareColumn", "Greater", "long", "double", ">"},
      {"ColumnCompareColumn", "Greater", "double", "double", ">"},
      {"ColumnCompareColumn", "GreaterEqual", "long", "double", ">="},
      {"ColumnCompareColumn", "GreaterEqual", "double", "double", ">="},

      {"ColumnCompareColumn", "Equal", "long", "long", "=="},
      {"ColumnCompareColumn", "Equal", "double", "long", "=="},
      {"ColumnCompareColumn", "NotEqual", "long", "long", "!="},
      {"ColumnCompareColumn", "NotEqual", "double", "long", "!="},
      {"ColumnCompareColumn", "Less", "long", "long", "<"},
      {"ColumnCompareColumn", "Less", "double", "long", "<"},
      {"ColumnCompareColumn", "LessEqual", "long", "long", "<="},
      {"ColumnCompareColumn", "LessEqual", "double", "long", "<="},
      {"ColumnCompareColumn", "Greater", "long", "long", ">"},
      {"ColumnCompareColumn", "Greater", "double", "long", ">"},
      {"ColumnCompareColumn", "GreaterEqual", "long", "long", ">="},
      {"ColumnCompareColumn", "GreaterEqual", "double", "long", ">="},

      // template, <ClassNamePrefix>, <ReturnType>, <OperandType>, <FuncName>, <OperandCast>,
      //   <ResultCast>
      {"ColumnUnaryFunc", "FuncRound", "double", "double", "MathExpr.round", "", ""},
      // round(longCol) returns a long and is a no-op. So it will not be implemented here.
      // round(Col, N) is a special case and will be implemented separately from this template
      {"ColumnUnaryFunc", "FuncFloor", "long", "double", "Math.floor", "", "(long)"},
      // Note: floor(long) is a no-op so code generation should remove it or use
      // an IdentityExpression
      {"ColumnUnaryFunc", "FuncCeil", "long", "double", "Math.ceil", "", "(long)"},
      // Similarly, ceil(long) is a no-op, so not generating code for it here
      {"ColumnUnaryFunc", "FuncExp", "double", "double", "Math.exp", "", ""},
      {"ColumnUnaryFunc", "FuncLn", "double", "double", "Math.log", "", ""},
      {"ColumnUnaryFunc", "FuncLn", "double", "long", "Math.log", "(double)", ""},
      {"ColumnUnaryFunc", "FuncLog10", "double", "double", "Math.log10", "", ""},
      {"ColumnUnaryFunc", "FuncLog10", "double", "long", "Math.log10", "(double)", ""},
      // The MathExpr class contains helper functions for cases when existing library
      // routines can't be used directly.
      {"ColumnUnaryFunc", "FuncLog2", "double", "double", "MathExpr.log2", "", ""},
      {"ColumnUnaryFunc", "FuncLog2", "double", "long", "MathExpr.log2", "(double)", ""},
      // Log(base, Col) is a special case and will be implemented separately from this template
      // Pow(col, P) and Power(col, P) are special cases implemented separately from this template
      {"ColumnUnaryFunc", "FuncSqrt", "double", "double", "Math.sqrt", "", ""},
      {"ColumnUnaryFunc", "FuncSqrt", "double", "long", "Math.sqrt", "(double)", ""},
      {"ColumnUnaryFunc", "FuncAbs", "double", "double", "Math.abs", "", ""},
      {"ColumnUnaryFunc", "FuncAbs", "long", "long", "MathExpr.abs", "", ""},
      {"ColumnUnaryFunc", "FuncSin", "double", "double", "Math.sin", "", ""},
      {"ColumnUnaryFunc", "FuncASin", "double", "double", "Math.asin", "", ""},
      {"ColumnUnaryFunc", "FuncCos", "double", "double", "Math.cos", "", ""},
      {"ColumnUnaryFunc", "FuncACos", "double", "double", "Math.acos", "", ""},
      {"ColumnUnaryFunc", "FuncTan", "double", "double", "Math.tan", "", ""},
      {"ColumnUnaryFunc", "FuncATan", "double", "double", "Math.atan", "", ""},
      {"ColumnUnaryFunc", "FuncDegrees", "double", "double", "Math.toDegrees", "", ""},
      {"ColumnUnaryFunc", "FuncRadians", "double", "double", "Math.toRadians", "", ""},
      {"ColumnUnaryFunc", "FuncSign", "double", "double", "MathExpr.sign", "", ""},
      {"ColumnUnaryFunc", "FuncSign", "double", "long", "MathExpr.sign", "", ""},


        {"ColumnUnaryMinus", "long"},
        {"ColumnUnaryMinus", "double"},

      // template, <ClassName>, <ValueType>, <OperatorSymbol>, <DescriptionName>, <DescriptionValue>
      {"VectorUDAFMinMax", "VectorUDAFMinLong", "long", "<", "min",
          "_FUNC_(expr) - Returns the minimum value of expr (vectorized, type: long)"},
      {"VectorUDAFMinMax", "VectorUDAFMinDouble", "double", "<", "min",
          "_FUNC_(expr) - Returns the minimum value of expr (vectorized, type: double)"},
      {"VectorUDAFMinMax", "VectorUDAFMaxLong", "long", ">", "max",
          "_FUNC_(expr) - Returns the maximum value of expr (vectorized, type: long)"},
      {"VectorUDAFMinMax", "VectorUDAFMaxDouble", "double", ">", "max",
          "_FUNC_(expr) - Returns the maximum value of expr (vectorized, type: double)"},

      {"VectorUDAFMinMaxString", "VectorUDAFMinString", "<", "min",
          "_FUNC_(expr) - Returns the minimum value of expr (vectorized, type: string)"},
      {"VectorUDAFMinMaxString", "VectorUDAFMaxString", ">", "max",
          "_FUNC_(expr) - Returns the minimum value of expr (vectorized, type: string)"},

        //template, <ClassName>, <ValueType>
        {"VectorUDAFSum", "VectorUDAFSumLong", "long"},
        {"VectorUDAFSum", "VectorUDAFSumDouble", "double"},
        {"VectorUDAFAvg", "VectorUDAFAvgLong", "long"},
        {"VectorUDAFAvg", "VectorUDAFAvgDouble", "double"},

      // template, <ClassName>, <ValueType>, <VarianceFormula>, <DescriptionName>,
      // <DescriptionValue>
      {"VectorUDAFVar", "VectorUDAFVarPopLong", "long", "myagg.variance / myagg.count",
          "variance, var_pop",
          "_FUNC_(x) - Returns the variance of a set of numbers (vectorized, long)"},
      {"VectorUDAFVar", "VectorUDAFVarPopDouble", "double", "myagg.variance / myagg.count",
          "variance, var_pop",
          "_FUNC_(x) - Returns the variance of a set of numbers (vectorized, double)"},
      {"VectorUDAFVar", "VectorUDAFVarSampLong", "long", "myagg.variance / (myagg.count-1.0)",
          "var_samp",
          "_FUNC_(x) - Returns the sample variance of a set of numbers (vectorized, long)"},
      {"VectorUDAFVar", "VectorUDAFVarSampDouble", "double", "myagg.variance / (myagg.count-1.0)",
          "var_samp",
          "_FUNC_(x) - Returns the sample variance of a set of numbers (vectorized, double)"},
      {"VectorUDAFVar", "VectorUDAFStdPopLong", "long",
          "Math.sqrt(myagg.variance / (myagg.count))", "std,stddev,stddev_pop",
          "_FUNC_(x) - Returns the standard deviation of a set of numbers (vectorized, long)"},
      {"VectorUDAFVar", "VectorUDAFStdPopDouble", "double",
          "Math.sqrt(myagg.variance / (myagg.count))", "std,stddev,stddev_pop",
          "_FUNC_(x) - Returns the standard deviation of a set of numbers (vectorized, double)"},
      {"VectorUDAFVar", "VectorUDAFStdSampLong", "long",
          "Math.sqrt(myagg.variance / (myagg.count-1.0))", "stddev_samp",
          "_FUNC_(x) - Returns the sample standard deviation of a set of numbers (vectorized, long)"},
      {"VectorUDAFVar", "VectorUDAFStdSampDouble", "double",
          "Math.sqrt(myagg.variance / (myagg.count-1.0))", "stddev_samp",
          "_FUNC_(x) - Returns the sample standard deviation of a set of numbers (vectorized, double)"},

    };


  private String templateBaseDir;
  private String buildDir;

  private String expressionOutputDirectory;
  private String expressionTemplateDirectory;
  private String udafOutputDirectory;
  private String udafTemplateDirectory;
  private GenVectorTestCode testCodeGen;

  static String joinPath(String...parts) {
    String path = parts[0];
    for (int i=1; i < parts.length; ++i) {
      path += File.separatorChar + parts[i];
    }
    return path;
  }

  public void init(String templateBaseDir, String buildDir) {
    File generationDirectory = new File(templateBaseDir);

    String buildPath = joinPath(buildDir, "ql", "gen", "vector");

    File exprOutput = new File(joinPath(buildPath, "org", "apache", "hadoop",
        "hive", "ql", "exec", "vector", "expressions", "gen"));
    expressionOutputDirectory = exprOutput.getAbsolutePath();

    expressionTemplateDirectory =
        joinPath(generationDirectory.getAbsolutePath(), "ExpressionTemplates");

    File udafOutput = new File(joinPath(buildPath, "org", "apache", "hadoop",
        "hive", "ql", "exec", "vector", "expressions", "aggregates", "gen"));
    udafOutputDirectory = udafOutput.getAbsolutePath();

    udafTemplateDirectory =
        joinPath(generationDirectory.getAbsolutePath(), "UDAFTemplates");

    File testCodeOutput =
        new File(
            joinPath(buildDir, "ql", "test", "src", "org",
                "apache", "hadoop", "hive", "ql", "exec", "vector",
                "expressions", "gen"));
    testCodeGen = new GenVectorTestCode(testCodeOutput.getAbsolutePath(),
        joinPath(generationDirectory.getAbsolutePath(), "TestTemplates"));
  }

  /**
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    GenVectorCode gen = new GenVectorCode();
    gen.init(System.getProperty("user.dir"),
        joinPath(System.getProperty("user.dir"), "..", "..", "..", "..", "build"));
    gen.generate();
  }

  @Override
  public void execute() throws BuildException {
    init(templateBaseDir, buildDir);
    try {
      this.generate();
    } catch (Exception e) {
      new BuildException(e);
    }
  }

  private void generate() throws Exception {
    System.out.println("Generating vector expression code");
    for (String [] tdesc : templateExpansions) {
      if (tdesc[0].equals("ColumnArithmeticScalar")) {
        generateColumnArithmeticScalar(tdesc);
      } else if (tdesc[0].equals("ColumnCompareScalar")) {
        generateColumnCompareScalar(tdesc);
      } else if (tdesc[0].equals("ScalarCompareColumn")) {
        generateScalarCompareColumn(tdesc);
      } else if (tdesc[0].equals("FilterColumnCompareScalar")) {
        generateFilterColumnCompareScalar(tdesc);
      } else if (tdesc[0].equals("FilterScalarCompareColumn")) {
        generateFilterScalarCompareColumn(tdesc);
      } else if (tdesc[0].equals("ScalarArithmeticColumn")) {
        generateScalarArithmeticColumn(tdesc);
      } else if (tdesc[0].equals("FilterColumnCompareColumn")) {
        generateFilterColumnCompareColumn(tdesc);
      } else if (tdesc[0].equals("ColumnCompareColumn")) {
        generateColumnCompareColumn(tdesc);
      } else if (tdesc[0].equals("ColumnArithmeticColumn")) {
        generateColumnArithmeticColumn(tdesc);
      } else if (tdesc[0].equals("ColumnUnaryMinus")) {
        generateColumnUnaryMinus(tdesc);
      } else if (tdesc[0].equals("ColumnUnaryFunc")) {
        generateColumnUnaryFunc(tdesc);
      } else if (tdesc[0].equals("VectorUDAFMinMax")) {
        generateVectorUDAFMinMax(tdesc);
      } else if (tdesc[0].equals("VectorUDAFMinMaxString")) {
        generateVectorUDAFMinMaxString(tdesc);
      } else if (tdesc[0].equals("VectorUDAFSum")) {
        generateVectorUDAFSum(tdesc);
      } else if (tdesc[0].equals("VectorUDAFAvg")) {
        generateVectorUDAFAvg(tdesc);
      } else if (tdesc[0].equals("VectorUDAFVar")) {
        generateVectorUDAFVar(tdesc);
      } else if (tdesc[0].equals("FilterStringColumnCompareScalar")) {
        generateFilterStringColumnCompareScalar(tdesc);
      } else if (tdesc[0].equals("StringColumnCompareScalar")) {
        generateStringColumnCompareScalar(tdesc);
      } else if (tdesc[0].equals("FilterStringScalarCompareColumn")) {
        generateFilterStringScalarCompareColumn(tdesc);
      } else if (tdesc[0].equals("StringScalarCompareColumn")) {
        generateStringScalarCompareColumn(tdesc);
      } else if (tdesc[0].equals("FilterStringColumnCompareColumn")) {
        generateFilterStringColumnCompareColumn(tdesc);
      } else if (tdesc[0].equals("StringColumnCompareColumn")) {
        generateStringColumnCompareColumn(tdesc);
      } else {
        continue;
      }
    }
    System.out.println("Generating vector expression test code");
    testCodeGen.generateTestSuites();
  }

  private void generateColumnCompareColumn(String[] tdesc) throws IOException {
    //The variables are all same as ColumnCompareScalar except that
    //this template doesn't need a return type. Pass anything as return type.
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = getCamelCaseType(operandType1)
        + "Col" + operatorName + getCamelCaseType(operandType2) + "Column";
    generateColumnBinaryOperatorColumn(tdesc, "long", className);
  }

  private void generateVectorUDAFMinMax(String[] tdesc) throws Exception {
    String className = tdesc[1];
    String valueType = tdesc[2];
    String operatorSymbol = tdesc[3];
    String descName = tdesc[4];
    String descValue = tdesc[5];
    String columnType = getColumnVectorType(valueType);
    String writableType = getOutputWritableType(valueType);
    String inspectorType = getOutputObjectInspector(valueType);

    String outputFile = joinPath(this.udafOutputDirectory, className + ".java");
    String templateFile = joinPath(this.udafTemplateDirectory, tdesc[0] + ".txt");

    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<ValueType>", valueType);
    templateString = templateString.replaceAll("<OperatorSymbol>", operatorSymbol);
    templateString = templateString.replaceAll("<InputColumnVectorType>", columnType);
    templateString = templateString.replaceAll("<DescriptionName>", descName);
    templateString = templateString.replaceAll("<DescriptionValue>", descValue);
    templateString = templateString.replaceAll("<OutputType>", writableType);
    templateString = templateString.replaceAll("<OutputTypeInspector>", inspectorType);
    writeFile(outputFile, templateString);

  }

  private void generateVectorUDAFMinMaxString(String[] tdesc) throws Exception {
    String className = tdesc[1];
    String operatorSymbol = tdesc[2];
    String descName = tdesc[3];
    String descValue = tdesc[4];

    String outputFile = joinPath(this.udafOutputDirectory, className + ".java");
    String templateFile = joinPath(this.udafTemplateDirectory, tdesc[0] + ".txt");

    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<OperatorSymbol>", operatorSymbol);
    templateString = templateString.replaceAll("<DescriptionName>", descName);
    templateString = templateString.replaceAll("<DescriptionValue>", descValue);
    writeFile(outputFile, templateString);

  }

  private void generateVectorUDAFSum(String[] tdesc) throws Exception {
  //template, <ClassName>, <ValueType>, <OutputType>, <OutputTypeInspector>
    String className = tdesc[1];
    String valueType = tdesc[2];
    String columnType = getColumnVectorType(valueType);
    String writableType = getOutputWritableType(valueType);
    String inspectorType = getOutputObjectInspector(valueType);

    String outputFile = joinPath(this.udafOutputDirectory, className + ".java");
    String templateFile = joinPath(this.udafTemplateDirectory, tdesc[0] + ".txt");

    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<ValueType>", valueType);
    templateString = templateString.replaceAll("<InputColumnVectorType>", columnType);
    templateString = templateString.replaceAll("<OutputType>", writableType);
    templateString = templateString.replaceAll("<OutputTypeInspector>", inspectorType);
    writeFile(outputFile, templateString);
  }

  private void generateVectorUDAFAvg(String[] tdesc) throws IOException {
    String className = tdesc[1];
    String valueType = tdesc[2];
    String columnType = getColumnVectorType(valueType);

    String outputFile = joinPath(this.udafOutputDirectory, className + ".java");
    String templateFile = joinPath(this.udafTemplateDirectory, tdesc[0] + ".txt");

    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<ValueType>", valueType);
    templateString = templateString.replaceAll("<InputColumnVectorType>", columnType);
    writeFile(outputFile, templateString);
  }

  private void generateVectorUDAFVar(String[] tdesc) throws IOException {
    String className = tdesc[1];
    String valueType = tdesc[2];
    String varianceFormula = tdesc[3];
    String descriptionName = tdesc[4];
    String descriptionValue = tdesc[5];
    String columnType = getColumnVectorType(valueType);

    String outputFile = joinPath(this.udafOutputDirectory, className + ".java");
    String templateFile = joinPath(this.udafTemplateDirectory, tdesc[0] + ".txt");

    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<ValueType>", valueType);
    templateString = templateString.replaceAll("<InputColumnVectorType>", columnType);
    templateString = templateString.replaceAll("<VarianceFormula>", varianceFormula);
    templateString = templateString.replaceAll("<DescriptionName>", descriptionName);
    templateString = templateString.replaceAll("<DescriptionValue>", descriptionValue);
    writeFile(outputFile, templateString);
  }

  private void generateFilterStringScalarCompareColumn(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String className = "FilterStringScalar" + operatorName + "StringColumn";

    // Template expansion logic is the same for both column-scalar and scalar-column cases.
    generateStringColumnCompareScalar(tdesc, className);
  }

  private void generateStringScalarCompareColumn(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String className = "StringScalar" + operatorName + "StringColumn";

    // Template expansion logic is the same for both column-scalar and scalar-column cases.
    generateStringColumnCompareScalar(tdesc, className);
  }

  private void generateFilterStringColumnCompareScalar(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String className = "FilterStringCol" + operatorName + "StringScalar";
    generateStringColumnCompareScalar(tdesc, className);
  }

  private void generateStringColumnCompareScalar(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String className = "StringCol" + operatorName + "StringScalar";
    generateStringColumnCompareScalar(tdesc, className);
  }

  private void generateFilterStringColumnCompareColumn(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String className = "FilterStringCol" + operatorName + "StringColumn";
    generateStringColumnCompareScalar(tdesc, className);
  }

  private void generateStringColumnCompareColumn(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String className = "StringCol" + operatorName + "StringColumn";
    generateStringColumnCompareScalar(tdesc, className);
  }

  private void generateStringColumnCompareScalar(String[] tdesc, String className)
      throws IOException {
   String operatorSymbol = tdesc[2];
   String outputFile = joinPath(this.expressionOutputDirectory, className + ".java");
   // Read the template into a string;
   String templateFile = joinPath(this.expressionTemplateDirectory, tdesc[0] + ".txt");
   String templateString = readFile(templateFile);
   // Expand, and write result
   templateString = templateString.replaceAll("<ClassName>", className);
   templateString = templateString.replaceAll("<OperatorSymbol>", operatorSymbol);
   writeFile(outputFile, templateString);
  }

  private void generateFilterColumnCompareColumn(String[] tdesc) throws IOException {
    //The variables are all same as ColumnCompareScalar except that
    //this template doesn't need a return type. Pass anything as return type.
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = "Filter" + getCamelCaseType(operandType1)
        + "Col" + operatorName + getCamelCaseType(operandType2) + "Column";
    generateColumnBinaryOperatorColumn(tdesc, null, className);
  }

  private void generateColumnUnaryMinus(String[] tdesc) throws IOException {
    String operandType = tdesc[1];
    String inputColumnVectorType = this.getColumnVectorType(operandType);
    String outputColumnVectorType = inputColumnVectorType;
    String returnType = operandType;
    String className = getCamelCaseType(operandType) + "ColUnaryMinus";
    String outputFile = joinPath(this.expressionOutputDirectory, className + ".java");
    String templateFile = joinPath(this.expressionTemplateDirectory, tdesc[0] + ".txt");
    String templateString = readFile(templateFile);
    // Expand, and write result
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<InputColumnVectorType>", inputColumnVectorType);
    templateString = templateString.replaceAll("<OutputColumnVectorType>", outputColumnVectorType);
    templateString = templateString.replaceAll("<OperandType>", operandType);
    templateString = templateString.replaceAll("<ReturnType>", returnType);
    writeFile(outputFile, templateString);
  }

  // template, <ClassNamePrefix>, <ReturnType>, <OperandType>, <FuncName>, <OperandCast>, <ResultCast>
  private void generateColumnUnaryFunc(String[] tdesc) throws IOException {
    String classNamePrefix = tdesc[1];
    String operandType = tdesc[3];
    String inputColumnVectorType = this.getColumnVectorType(operandType);
    String returnType = tdesc[2];
    String outputColumnVectorType = this.getColumnVectorType(returnType);
    String className = classNamePrefix + getCamelCaseType(operandType) + "To"
      + getCamelCaseType(returnType);
    String outputFile = joinPath(this.expressionOutputDirectory, className + ".java");
    String templateFile = joinPath(this.expressionTemplateDirectory, tdesc[0] + ".txt");
    String templateString = readFile(templateFile);
    String funcName = tdesc[4];
    String operandCast = tdesc[5];
    String resultCast = tdesc[6];
    // Expand, and write result
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<InputColumnVectorType>", inputColumnVectorType);
    templateString = templateString.replaceAll("<OutputColumnVectorType>", outputColumnVectorType);
    templateString = templateString.replaceAll("<OperandType>", operandType);
    templateString = templateString.replaceAll("<ReturnType>", returnType);
    templateString = templateString.replaceAll("<FuncName>", funcName);
    templateString = templateString.replaceAll("<OperandCast>", operandCast);
    templateString = templateString.replaceAll("<ResultCast>", resultCast);
    writeFile(outputFile, templateString);
  }

  private void generateColumnArithmeticColumn(String [] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = getCamelCaseType(operandType1)
        + "Col" + operatorName + getCamelCaseType(operandType2) + "Column";
    String returnType = getArithmeticReturnType(operandType1, operandType2);
    generateColumnBinaryOperatorColumn(tdesc, returnType, className);
  }

  private void generateFilterColumnCompareScalar(String[] tdesc) throws IOException {
    //The variables are all same as ColumnCompareScalar except that
    //this template doesn't need a return type. Pass anything as return type.
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = "Filter" + getCamelCaseType(operandType1)
        + "Col" + operatorName + getCamelCaseType(operandType2) + "Scalar";
    generateColumnBinaryOperatorScalar(tdesc, null, className);
  }

  private void generateFilterScalarCompareColumn(String[] tdesc) throws IOException {
    //this template doesn't need a return type. Pass anything as return type.
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = "Filter" + getCamelCaseType(operandType1)
        + "Scalar" + operatorName + getCamelCaseType(operandType2) + "Column";
    generateScalarBinaryOperatorColumn(tdesc, null, className);
  }

  private void generateColumnCompareScalar(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String returnType = "long";
    String className = getCamelCaseType(operandType1)
        + "Col" + operatorName + getCamelCaseType(operandType2) + "Scalar";
    generateColumnBinaryOperatorScalar(tdesc, returnType, className);
  }

  private void generateScalarCompareColumn(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String returnType = "long";
    String className = getCamelCaseType(operandType1)
        + "Scalar" + operatorName + getCamelCaseType(operandType2) + "Column";
    generateScalarBinaryOperatorColumn(tdesc, returnType, className);
  }

  private void generateColumnBinaryOperatorColumn(String[] tdesc, String returnType,
         String className) throws IOException {
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String outputColumnVectorType = this.getColumnVectorType(returnType);
    String inputColumnVectorType1 = this.getColumnVectorType(operandType1);
    String inputColumnVectorType2 = this.getColumnVectorType(operandType2);
    String operatorSymbol = tdesc[4];
    String outputFile = joinPath(this.expressionOutputDirectory, className + ".java");

    //Read the template into a string;
    String templateFile = joinPath(this.expressionTemplateDirectory, tdesc[0] + ".txt");
    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<InputColumnVectorType1>", inputColumnVectorType1);
    templateString = templateString.replaceAll("<InputColumnVectorType2>", inputColumnVectorType2);
    templateString = templateString.replaceAll("<OutputColumnVectorType>", outputColumnVectorType);
    templateString = templateString.replaceAll("<OperatorSymbol>", operatorSymbol);
    templateString = templateString.replaceAll("<OperandType1>", operandType1);
    templateString = templateString.replaceAll("<OperandType2>", operandType2);
    templateString = templateString.replaceAll("<ReturnType>", returnType);
    templateString = templateString.replaceAll("<CamelReturnType>", getCamelCaseType(returnType));
    writeFile(outputFile, templateString);

    if(returnType==null){
      testCodeGen.addColumnColumnFilterTestCases(
          className,
          inputColumnVectorType1,
          inputColumnVectorType2,
          operatorSymbol);
    }else{
      testCodeGen.addColumnColumnOperationTestCases(
          className,
          inputColumnVectorType1,
          inputColumnVectorType2,
          outputColumnVectorType);
    }
  }

  private void generateColumnBinaryOperatorScalar(String[] tdesc, String returnType,
     String className) throws IOException {
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String outputColumnVectorType = this.getColumnVectorType(returnType);
    String inputColumnVectorType = this.getColumnVectorType(operandType1);
    String operatorSymbol = tdesc[4];
    String outputFile = joinPath(this.expressionOutputDirectory, className + ".java");

    //Read the template into a string;
    String templateFile = joinPath(this.expressionTemplateDirectory, tdesc[0] + ".txt");
    String templateString = readFile(templateFile);
    templateString = templateString.replaceAll("<ClassName>", className);
    templateString = templateString.replaceAll("<InputColumnVectorType>", inputColumnVectorType);
    templateString = templateString.replaceAll("<OutputColumnVectorType>", outputColumnVectorType);
    templateString = templateString.replaceAll("<OperatorSymbol>", operatorSymbol);
    templateString = templateString.replaceAll("<OperandType1>", operandType1);
    templateString = templateString.replaceAll("<OperandType2>", operandType2);
    templateString = templateString.replaceAll("<ReturnType>", returnType);
    writeFile(outputFile, templateString);

    if(returnType==null) {
      testCodeGen.addColumnScalarFilterTestCases(
          true,
          className,
          inputColumnVectorType,
          operandType2,
          operatorSymbol);
    } else {
      testCodeGen.addColumnScalarOperationTestCases(
          true,
          className,
          inputColumnVectorType,
          outputColumnVectorType,
          operandType2);
    }
  }

  private void generateScalarBinaryOperatorColumn(String[] tdesc, String returnType,
     String className) throws IOException {
     String operandType1 = tdesc[2];
     String operandType2 = tdesc[3];
     String outputColumnVectorType = this.getColumnVectorType(returnType);
     String inputColumnVectorType = this.getColumnVectorType(operandType2);
     String operatorSymbol = tdesc[4];
     String outputFile = joinPath(this.expressionOutputDirectory, className + ".java");

     //Read the template into a string;
     String templateFile = joinPath(this.expressionTemplateDirectory, tdesc[0] + ".txt");
     String templateString = readFile(templateFile);
     templateString = templateString.replaceAll("<ClassName>", className);
     templateString = templateString.replaceAll("<InputColumnVectorType>", inputColumnVectorType);
     templateString = templateString.replaceAll("<OutputColumnVectorType>", outputColumnVectorType);
     templateString = templateString.replaceAll("<OperatorSymbol>", operatorSymbol);
     templateString = templateString.replaceAll("<OperandType1>", operandType1);
     templateString = templateString.replaceAll("<OperandType2>", operandType2);
     templateString = templateString.replaceAll("<ReturnType>", returnType);
     writeFile(outputFile, templateString);

     if(returnType==null) {
       testCodeGen.addColumnScalarFilterTestCases(
           false,
           className,
           inputColumnVectorType,
           operandType1,
           operatorSymbol);
     } else {
       testCodeGen.addColumnScalarOperationTestCases(
           false,
           className,
           inputColumnVectorType,
           outputColumnVectorType,
           operandType1);
     }
   }

  //Binary arithmetic operator
  private void generateColumnArithmeticScalar(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = getCamelCaseType(operandType1)
        + "Col" + operatorName + getCamelCaseType(operandType2) + "Scalar";
    String returnType = getArithmeticReturnType(operandType1, operandType2);
    generateColumnBinaryOperatorScalar(tdesc, returnType, className);
  }

  private void generateScalarArithmeticColumn(String[] tdesc) throws IOException {
    String operatorName = tdesc[1];
    String operandType1 = tdesc[2];
    String operandType2 = tdesc[3];
    String className = getCamelCaseType(operandType1)
        + "Scalar" + operatorName + getCamelCaseType(operandType2) + "Column";
    String returnType = getArithmeticReturnType(operandType1, operandType2);
    generateScalarBinaryOperatorColumn(tdesc, returnType, className);
  }


   static void writeFile(String outputFile, String str) throws IOException {
    BufferedWriter w = new BufferedWriter(new FileWriter(outputFile));
    w.write(str);
    w.close();
  }

   static String readFile(String templateFile) throws IOException {
    BufferedReader r = new BufferedReader(new FileReader(templateFile));
    String line = r.readLine();
    StringBuilder b = new StringBuilder();
    while (line != null) {
      b.append(line);
      b.append("\n");
      line = r.readLine();
    }
    r.close();
    return b.toString();
  }

  static String getCamelCaseType(String type) {
    if (type == null) {
      return null;
    }
    if (type.equals("long")) {
      return "Long";
    } else if (type.equals("double")) {
      return "Double";
    } else {
      return type;
    }
  }

  private String getArithmeticReturnType(String operandType1,
      String operandType2) {
    if (operandType1.equals("double") ||
        operandType2.equals("double")) {
      return "double";
    } else {
      return "long";
    }
  }

  private String getColumnVectorType(String primitiveType) {
    if(primitiveType!=null && primitiveType.equals("double")) {
      return "DoubleColumnVector";
    }
    return "LongColumnVector";
  }

  private String getOutputWritableType(String primitiveType) throws Exception {
    if (primitiveType.equals("long")) {
      return "LongWritable";
    } else if (primitiveType.equals("double")) {
      return "DoubleWritable";
    }
    throw new Exception("Unimplemented primitive output writable: " + primitiveType);
  }

  private String getOutputObjectInspector(String primitiveType) throws Exception {
    if (primitiveType.equals("long")) {
      return "PrimitiveObjectInspectorFactory.writableLongObjectInspector";
    } else if (primitiveType.equals("double")) {
      return "PrimitiveObjectInspectorFactory.writableDoubleObjectInspector";
    }
    throw new Exception("Unimplemented primitive output inspector: " + primitiveType);
  }

  public void setTemplateBaseDir(String templateBaseDir) {
    this.templateBaseDir = templateBaseDir;
  }

  public void setBuildDir(String buildDir) {
    this.buildDir = buildDir;
  }
}

