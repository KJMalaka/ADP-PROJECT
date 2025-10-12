package domain;

import java.io.Serializable;

/**
 * Data Transfer Object for client requests
 * Used for client-server communication with custom serialization
 */
public class RequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestType;
    private Object payload;

    public RequestDTO() {}

    public RequestDTO(String requestType, Object payload) {
        this.requestType = requestType;
        this.payload = payload;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "RequestDTO{" +
                "requestType='" + requestType + '\'' +
                ", payload=" + payload +
                '}';
    }
}
