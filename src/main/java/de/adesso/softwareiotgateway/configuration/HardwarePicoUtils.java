package de.adesso.softwareiotgateway.configuration;

import java.util.List;

public class HardwarePicoUtils {

    public static boolean idAlreadyInList(List<String> hardwarePicoUris, String toSearch){
        if(hardwarePicoUris != null && !hardwarePicoUris.isEmpty() && toSearch != null){
            for(String uri : hardwarePicoUris){
                if(uri.split("/")[0].equals(toSearch.split("/")[0])){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean equalIds(String uri1, String uri2){
        return uri1.split("/")[0].equals(uri2.split("/")[0]);
    }

}
