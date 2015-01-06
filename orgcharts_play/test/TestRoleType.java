import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import role.*;

/**
 * Just want to try Enum type of annotation.
 * It has noting to do with the main program.
 *
 */
public class TestRoleType {

    @AcademicRolesAllowed({AcademicRoleType.DEANERY})
    public class DeaneryDemo {

    }

    @Test
    public void testDeanery() {
        List<RoleType> roleTypes = RolesAllowedUtil.getRoleTypesAllowedFromAnnotations(DeaneryDemo.class.getAnnotations());
        assertEquals(1, roleTypes.size());
    }

}
