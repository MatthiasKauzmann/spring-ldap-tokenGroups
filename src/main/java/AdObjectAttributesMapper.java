import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.support.LdapUtils;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the tokenGroups-attribute data structure in AD. 
 *
 * @author Matthias Kauzmann
 */
public class AdObjectAttributesMapper implements AttributesMapper<AdObject> {

    /**
    * Map only the tokenGroups-attribute from AD to an instance of AdObject.
    * Do this by reading every tokenGroups entry as a byte array, convert it into its 
    * string representation and channel it into AdObject's tokenGroups field.
    *
    * @param attributes that are being fetched from AD
    * @return an instance of AdObject with a populated tokenGroups field
    */
    @Override
    public AdObject mapFromAttributes(Attributes attributes) throws NamingException {

        AdObject adObject = new AdObject();
        List<String> sidList = new ArrayList<>();
        NamingEnumeration<?> namingEnumeration = attributes.get("tokenGroups").getAll();
        while (namingEnumeration.hasMore()) {
            sidList.add(LdapUtils.convertBinarySidToString((byte[]) namingEnumeration.next()));
        }
        adObject.setTokenGroups(sidList);
        return adObject;
    }

}
