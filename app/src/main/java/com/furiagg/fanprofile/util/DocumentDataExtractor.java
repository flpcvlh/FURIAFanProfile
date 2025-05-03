package com.furiagg.fanprofile.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentDataExtractor {

    private String documentText;

    public DocumentDataExtractor(String documentText) {
        this.documentText = documentText;
    }

    public Map<String, String> extractData() {
        Map<String, String> data = new HashMap<>();

        // Extrair nome
        extractName(data);

        // Extrair CPF
        extractCpf(data);

        // Extrair data de nascimento
        extractBirthDate(data);

        return data;
    }

    private void extractName(Map<String, String> data) {
        // Padrões comuns para encontrar nome em documentos brasileiros
        String[] namePatterns = {
                "(?i)nome[:\\s]+([A-ZÀ-Ú\\s]+[A-ZÀ-Ú])",
                "(?i)nome civil[:\\s]+([A-ZÀ-Ú\\s]+[A-ZÀ-Ú])"
        };

        for (String pattern : namePatterns) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(documentText);

            if (m.find()) {
                String name = m.group(1).trim();
                data.put("name", name);
                return;
            }
        }

        // Se não encontrar por padrões, utilizar heurísticas
        // Em documentos, nomes geralmente estão em caixa alta
        Pattern upperCaseWords = Pattern.compile("([A-ZÀ-Ú]{2,}(\\s[A-ZÀ-Ú]{2,}){1,})");
        Matcher matcher = upperCaseWords.matcher(documentText);

        if (matcher.find()) {
            String potentialName = matcher.group(1).trim();
            // Filtrar palavras que não parecem ser parte de um nome
            if (potentialName.length() > 5 && !potentialName.contains("CPF") &&
                    !potentialName.contains("REPÚBLICA") && !potentialName.contains("FEDERATIVA") &&
                    !potentialName.contains("BRASIL")) {
                data.put("name", potentialName);
            }
        }
    }

    private void extractCpf(Map<String, String> data) {
        // Padrão para CPF: XXX.XXX.XXX-XX ou XXXXXXXXXXX
        String[] cpfPatterns = {
                "(?i)cpf[:\\s]*(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2})",
                "(?i)cpf[:\\s]*(\\d{11})"
        };

        for (String pattern : cpfPatterns) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(documentText);

            if (m.find()) {
                String cpf = m.group(1).trim();
                // Remover formatação se estiver formatado
                cpf = cpf.replaceAll("[^0-9]", "");
                data.put("cpf", cpf);
                return;
            }
        }

        // Busca genérica por números que podem ser CPF
        Pattern genericCpf = Pattern.compile("(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2})");
        Matcher matcher = genericCpf.matcher(documentText);

        if (matcher.find()) {
            String cpf = matcher.group(1).trim();
            cpf = cpf.replaceAll("[^0-9]", "");
            data.put("cpf", cpf);
        }
    }

    private void extractBirthDate(Map<String, String> data) {
        // Padrões para datas no formato dd/mm/yyyy ou dd.mm.yyyy
        String[] datePatterns = {
                "(?i)nasc[a-záàâãéèêíïóôõöúçñ]*[:\\s]*(\\d{2}/\\d{2}/\\d{4})",
                "(?i)nasc[a-záàâãéèêíïóôõöúçñ]*[:\\s]*(\\d{2}\\.\\d{2}\\.\\d{4})",
                "(?i)data de nascimento[:\\s]*(\\d{2}/\\d{2}/\\d{4})",
                "(?i)data de nascimento[:\\s]*(\\d{2}\\.\\d{2}\\.\\d{4})"
        };

        for (String pattern : datePatterns) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(documentText);

            if (m.find()) {
                String birthDate = m.group(1).trim();
                data.put("birthDate", birthDate);
                return;
            }
        }

        // Busca genérica por datas
        Pattern genericDate = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})");
        Matcher matcher = genericDate.matcher(documentText);

        if (matcher.find()) {
            String birthDate = matcher.group(1).trim();
            data.put("birthDate", birthDate);
        }
    }
}
