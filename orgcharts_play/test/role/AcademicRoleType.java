package role;


public enum AcademicRoleType implements RoleType {
    STUDENT, TEACHER, DEANERY;
    @Override
    public String toString() {
        return name();
    }
}