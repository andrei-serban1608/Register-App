public abstract class User implements Comparable {
    private String firstName, lastName;
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }
    public int compareTo(Object o) {
        if (o == null) {
            throw new NullPointerException("Null Argument!");
        }
        if (!(o instanceof User)) {
            throw new ClassCastException("Parameter given is not a User!");
        }
        return toString().compareTo(o.toString());
    }
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("Null argument");
        }
        if (! (obj instanceof User)) {
            throw new ClassCastException("Invalid type");
        }
        return toString().equals(obj.toString());
    }
    public String toString() {
        return firstName + " " + lastName;
    }
}
