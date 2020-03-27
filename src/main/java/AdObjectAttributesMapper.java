import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.support.LdapUtils;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.List;

public class AdObjectAttributesMapper implements AttributesMapper<AdObject> {

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
