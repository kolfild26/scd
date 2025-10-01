package ru.sportmaster.scd.ui.view;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

public class UIViewProvider extends ClassPathScanningCandidateComponentProvider {
    public UIViewProvider() {
        super(false);
    }
}
