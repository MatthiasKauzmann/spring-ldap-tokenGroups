# Get the Active Directory attribute ```tokenGroups``` in an easy fashion :raised_hands:

Recently at work I had to deal with Active Directory's (AD) attribute ```tokenGroups```. That's a tricky one.
You might have already figured that. It's kind of invisible, meaning one can not browse it in - let's say - a client like the 
Active Directory Explorer. That's b/c it's a so-called ```extended property```, which is only being generated by AD when
explicitly requested for. Therefore, regular queries - like for all objects related to a user f.e. - will not show it. AD filter 
syntax doesn't even support it as a filter attribute. :boom:

In the Java world one does not need to write LDAP syntax since frameworks like Spring LDAP - namely the LdapTemplate class -
provide many methods that translate into AD queries. Still, asking for that one specific attribute comes with a burden. The attribute 
tokenGroups is a list of byte arrays. List entries contain the ```objectSid``` of both direct and indirect (transient) authorities/memberships an user or group has. So if I was a member of 20 groups in my workplace my ```memberOf``` attribute in AD would have a count of 20. But if these groups were also members in some other groups - let's say in 10 - I'd have a total count of 30 (adding my indirect/transient memberships which is not displayed by the memberOf-attribute). This piece of information is hard to get! Trust me. 
:point_up:

There's no method called getTokenGroups(), not in Java or elsewhere. A collegue of mine who's a .NET-developer told me that there's a method called refreshCache() in C#. It refreshes AD's property cache which is key to get tokenGroups since the operation of gathering 
tokenGroups exceeds AD's short living cache: by the time AD has gathered one's tokenGroups it has already forgotten whose tokenGroups it
was :laughing: Nevertheless, .NET-developers do also need to implement a getTokenGroups()-method.

A naive approach would be to gather transient memberships by recursion. You would start with fetching an user's memberOf-attribute, which returns a list of distinguished names of the memberships. A ```distinguished name``` (DN) is the best filter one can provide for a LDAP search b/c it's the complete path, which is the fastest route to resources in hierarchical databases like AD. Despite the DN, this 
approach is not of high-performance. It's actually really slow. You would need to implement a recursive method that calls itself for each DN in a DN's memberOf-attribute and find memberships until every group's group and so forth is eventually gathered. :sleeping: 

When I turned to the internet for help i realized that there's hardly code that either works, is not deprecated or is code at all :grimacing: Here's my story, I desperately tried out every method in LdapTemplate that fetches data. There's a bunch of multiple search(), find() and lookup() methods with docs that don't really distinguish. Well, none of them claim to fetch extended properties. 
I'd totally do that if I provisioned these methods, hell yeah! duh :triumph: 

Eventually I got blessed and found the right one, set the right properties and triggered AD's built-in algorithm for gathering 
tokenGroups instead of getting them recursively. Save your nerves and check out my code :angel:
