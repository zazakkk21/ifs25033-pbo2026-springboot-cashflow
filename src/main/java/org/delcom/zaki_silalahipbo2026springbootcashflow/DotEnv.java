package org.delcom.zaki_silalahipbo2026springbootcashflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DotEnv{
  public static void load(){
    Path path = Path.of(".env");

    if(!Files.exists(path)){
      return;
    }

    try{
      List<String> lines = Files.readAllLines(path);

      for(String line : lines){
        line = line.trim();
        if(line.isEmpty() || line.startsWith("#")){
          continue;
        }

        String[] parts = line.split("=", 2);

        if(parts.length == 2){
          String key = parts[0].trim();
          String value = parts[1].trim();

          System.setProperty(key, value);
        }
      }
    } catch (IOException e){
      throw  new RuntimeException("Gagal membaca file .env", e);
    }
  }
}