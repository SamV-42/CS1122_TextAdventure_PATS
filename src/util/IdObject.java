package util;

public abstract class IdObject {

    private String id;
    private RegistrationComponent<IdObject> compRegistrationId = null;

    public IdObject(String className, String id, boolean register) {
        this.id = id;
        if(register) {
            String id_name = className + "_id";
            compRegistrationId = new RegistrationComponent<>(this, id_name, id);
        }
    }

    public String getId() {
        return id;
    }
}
