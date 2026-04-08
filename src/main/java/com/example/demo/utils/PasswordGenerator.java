package com.example.demo.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordGenerator {

    private static final String UPPERCASSE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS    = "0123456789";
    private static final String SYMBOLS   = "!@#$%&*";
    private static final String ALL = UPPERCASSE + LOWERCASE + NUMBERS + SYMBOLS;

    private static final int TAMANHO = 12;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        List<Character> senha = new ArrayList<>(obrigatorios());

        IntStream.range(senha.size(), TAMANHO)
                .mapToObj(i -> caracterAleatorio(ALL))
                .forEach(senha::add);

        Collections.shuffle(senha, RANDOM);

        return senha.stream()
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    private static List<Character> obrigatorios() {
        return List.of(
                caracterAleatorio(UPPERCASSE),
                caracterAleatorio(LOWERCASE),
                caracterAleatorio(NUMBERS),
                caracterAleatorio(SYMBOLS)
        );
    }

    private static char caracterAleatorio(String fonte) {
        return fonte.charAt(RANDOM.nextInt(fonte.length()));
    }
}