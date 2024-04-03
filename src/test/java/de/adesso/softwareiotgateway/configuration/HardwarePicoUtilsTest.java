package de.adesso.softwareiotgateway.configuration;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static de.adesso.softwareiotgateway.configuration.HardwarePicoUtils.*;

class HardwarePicoUtilsTest {

    @Test
    void isAlreadyInList(){
        List<String> uris = List.of("1/abc", "2/cde", "3/xyz");

        assertTrue(idAlreadyInList(uris, "1/xyz"));
        assertFalse(idAlreadyInList(uris, "4/abc"));
        assertFalse(idAlreadyInList(null, "1/abc"));
        assertFalse(idAlreadyInList(List.of(), "1/abc"));
        assertFalse(idAlreadyInList(uris, null));
        assertFalse(idAlreadyInList(uris, ""));
    }

    @Test
    void equalIds(){

        String s1 = "1/abc";
        String s2 = "2/abc";
        String s3 = "1/xyz";

        assertFalse(HardwarePicoUtils.equalIds(s1, s2));
        assertTrue(HardwarePicoUtils.equalIds(s1, s3));

    }

}