package org.example.demo;

import java.util.ArrayList;
import java.util.List;

public class SignalAnalyzer {
    private List<Double> coefficients = new ArrayList<>();

    public void addCoefficient(double coefficient) {
        coefficients.add(coefficient);
    }

    public double predictNextCoefficient() {
        // Логика для предсказания следующего коэффициента
        // Здесь может быть ваша модель машинного обучения или простая логика
        if (coefficients.isEmpty()) {
            return 0; // Возвращаем 0, если коэффициенты отсутствуют
        }
        // Простой пример: возвращаем среднее значение
        return coefficients.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public String generateSignal(double predictedCoefficient) {
        // Генерация сообщения на основе предсказанного коэффициента
        return "Следующий коэффициент: " + predictedCoefficient;
    }
}
