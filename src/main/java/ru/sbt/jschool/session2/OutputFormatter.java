/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.sbt.jschool.session2;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class OutputFormatter {
    private PrintStream out;

    private int getintlen(Integer n) {
        int k = 0;
        String tmp = n.toString();
        k = n.toString().length() * 4 / 3;
        if (n.toString().length() % 3 == 0 && k > 3) {
            k--;
        }
        return k;
    }

    private int getdoblen(Double n) {
        Integer i = n.intValue();
        int k = getintlen(i);
        return k + 3;
    }

    public OutputFormatter(PrintStream out) {
        this.out = out;
    }

    public void output(String[] names, Object[][] data) {
        // colnum - column width
        int[] colnum = new int[names.length];
        // firstly colnum fills with names width
        for (int i = 0; i < names.length; i++) {
            colnum[i] = names[i].length();
        }
        // secondly each column fills with max length of data
        for (int i = 0; i < data.length; i++) {
            Object[] ob = data[i];
            for (int j = 0; j < ob.length; j++) {
                if (ob[j] instanceof String) {
                    String s = (String) ob[j];
                    if (colnum[j] < s.length()) {
                        colnum[j] = s.length();
                    }
                }
                if (data[i][j] instanceof Integer) {
                    Integer n = (Integer) data[i][j];
                    int intlen = getintlen(n);
                    if (colnum[j] < intlen) {
                        colnum[j] = intlen;
                    }
                }
                if (data[i][j] instanceof Double) {
                    Double n = (Double) data[i][j];
                    int doblen = getdoblen(n);
                    if (colnum[j] < doblen) {
                        colnum[j] = doblen;
                    }
                }
                if (data[i][j] instanceof Date) {
                    colnum[j] = 10;
                }
            }
        }
        // print separator
        String sep = "+";
        for (int i = 0; i < colnum.length; i++) {
            for (int j = 0; j < colnum[i]; j++) {
                sep += "-";
            }
            sep += "+";
        }
        this.out.println(sep);
        // print names string
        this.out.print("|");
        for (int i = 0; i < names.length; i++) {
            printcenter(names[i], colnum[i]);
        }
        this.out.println("");
        // print data table
        for (int i = 0; i < data.length; i++) {
            // print separator
            this.out.println(sep);
            this.out.print("|");
            Object[] ob = data[i];
            for (int j = 0; j < ob.length; j++) {
                if (ob[j] == null) {
                    // if null - find not null in column
                    for (int k = 0; k < data.length; k++) {
                        if (data[k][j] != null) {
                            if (data[k][j] instanceof String) {
                                String format = "%" + -colnum[j] + "s";
                                this.out.printf(format, "-");
                                this.out.print("|");
                            } else {
                                String s = "-";
                                for (int l = 0; l < colnum[j] - 1; l++) {
                                    s = " " + s;
                                }
                                this.out.print(s + "|");
                            }
                            break;
                        }
                    }
                    continue;
                }
                DecimalFormat intFormat = new DecimalFormat("###,###");
                DecimalFormat floatFormat = new DecimalFormat("###,##0.00");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                if (ob[j] instanceof String) {
                    String s = (String) ob[j];
                    String format = "%" + -colnum[j] + "s";
                    this.out.printf(format, s);
                    this.out.print("|");
                }
                if (data[i][j] instanceof Integer) {
                    Integer n = (Integer) data[i][j];
                    String ints = "";
                    for (int k = getintlen(n); k < colnum[j]; k++) {
                        ints += " ";
                    }
                    ints += intFormat.format(n);
                    this.out.print(ints);
                    this.out.print("|");
                }
                if (data[i][j] instanceof Double) {
                    Double n = (Double) data[i][j];
                    String dobs = "";
                    for (int k = getdoblen(n); k < colnum[j]; k++) {
                        dobs += " ";
                    }
                    dobs += floatFormat.format(n);
                    this.out.print(dobs);
                    this.out.print("|");
                }
                if (data[i][j] instanceof Date) {
                    Date d = (Date) data[i][j];
                    this.out.print(dateFormat.format(d));
                    this.out.print("|");
                }
            }
            this.out.println("");
        }
        // print separator again
        this.out.println(sep);
    }

    private void printcenter(String s, int len) {
        int i = (len - s.length()) / 2;
        while (i > 0) {
            s = " " + s;
            i--;
        }
        this.out.printf("%" + -len + "s|", s);
    }
}
