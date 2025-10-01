package ru.sportmaster.scd.exceptions;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String uuid) {
        super("File for uuid=" + uuid + " not found");
    }
}
