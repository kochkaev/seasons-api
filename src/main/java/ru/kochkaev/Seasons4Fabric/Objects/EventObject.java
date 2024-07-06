public abstract class EventObject {
    
    private List<Method> methods = new ArrayList();

    public void addMethod(Method method) {
        methods.add(method);
    }

    public List<Method> getMethods() {
        return this.methods();
    }
}