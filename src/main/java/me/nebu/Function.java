package me.nebu;

import java.util.List;

public record Function(String module, String name, List<String> code, List<String> arguments) {}
