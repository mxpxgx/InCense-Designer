package edu.incense.designer.task.survey;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class JsonSurvey {
    public final static String SURVEY = "survey";
    public final static String ID = "id";
    public final static String TITLE = "title";
    public final static String QUESTIONS = "questions";

    private ObjectMapper mapper;

    public JsonSurvey() {
        mapper = new ObjectMapper(); // can reuse, share globally
    }

    public JsonSurvey(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public Survey toSurvey(String json) {
        JsonNode node = null;
        try {
            node = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            System.err.println(getClass().getName() + ": Parsing JSON file failed, " + e);
            return null;
        } catch (IOException e) {
            System.err.println(getClass().getName() + ": Parsing JSON file failed, " + e);
            return null;
        }
        return toSurvey(node);
    }
    
    public Survey toSurvey(JsonNode root) {
        Survey survey = null;
        try {
            survey = new Survey();

            JsonNode attribute = root.get(TITLE);
            survey.setTitle(attribute.getValueAsText());

            attribute = root.get(ID);
            survey.setId(attribute.getValueAsInt());

            attribute = root.get(QUESTIONS);
            List<Question> questions = mapper.readValue(attribute,
                    new TypeReference<List<Question>>() {
                    });
            if (questions != null)
                survey.setQuestions(questions);
            else {
                System.out.println(getClass().getName() +
                        ": Questions JSON node was empty/null or doesn't exist");
            }
        } catch (JsonParseException e) {
            System.err.println(getClass().getName() + ": Parsing JSON file failed, " + e);
            return null;
        } catch (JsonMappingException e) {
            System.err.println(getClass().getName() + ": Mapping JSON file failed, " + e);
            return null;
        } catch (IOException e) {
            System.err.println(getClass().getName() + ": Reading JSON file failed, " + e);
            return null;
        }
        return survey;
    }

}
