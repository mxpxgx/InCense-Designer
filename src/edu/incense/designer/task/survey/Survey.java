package edu.incense.designer.task.survey;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Survey implements Serializable {
    private static final long serialVersionUID = 5496128017069179229L;
    private static final String TAG = "Survey";
    private int id;
    private String title;
    private List<Question> questions;

    public void add(Question question) {
        if (questions == null) {
            questions = new ArrayList<Question>();
        }
        questions.add(question);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Question getQuestion(int index) {
        return questions.get(index);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return questions.size();
    }
    
    /* JSON METHODS */

    public JsonNode toJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(this);
        return node;
    }

    public String toJsonString() {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.valueToTree(this);
        String json = null;
        try {
            json = mapper.writeValueAsString(node);
        } catch (JsonGenerationException e) {
            System.err.println(TAG + ": " + e);
        } catch (JsonMappingException e) {
            System.err.println(TAG + ": " + e);
        } catch (IOException e) {
            System.err.println(TAG + ": " + e);
        }
        return json;
    }

}
