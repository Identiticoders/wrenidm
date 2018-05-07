# WrenIDM

WrenIDM enables you to consolidate multiple identity sources for policy and workflow-based management. WrenIDM can 
consume, transform and feed data to external sources so that you maintain control over the identities of users, 
devices and other objects.

WrenIDM provides standard RESTful interfaces to offer ultimate flexibility so that you can customize and develop the product to fit the
requirements of your deployment.

## Build The Source Code

In order to build the project from the command line follow these steps:

### Prepare your Environment
The environment you need to set up is dependent upon the version of WrenIDM that you want to build. To build WrenIDM you
will need the following installed on the machine you're going to build on;

Software               | Required Version
---------------------- | ----------------
Java JDK Version	Maven  | 8 and above (see below)
Git                    | 1.7.6 and above
Maven                  | 3.1.0 and above

You should also set the following environment variables for the majority of versions;

JAVA_HOME - set to the directory in which your SDK is installed  
MAVEN_OPTS  - When building with Java 7 set this to '-Xmx1g -XX:MaxPermSize=512m'. Java 8 and above does not support 
MaxPermSize so set this to '-Xmx1g'.

### Building the Code

The WrenIDM build process and dependencies are managed by Maven. The first time you build the project, Maven will pull 
down all the dependencies and Maven plugins required by the build, which can take a significant amount of time. 
Subsequent builds will be much faster!

```
$ cd $REPO_HOME/wrenidm
$ mvn -Dignore-artifact-sigs -DskipTests=true clean install
```


## License

This project is licensed under the Common Development and Distribution License (CDDL). The following text applies to 
both this file, and should also be included in all files in the project:

> The contents of this file are subject to the terms of the Common Development and  Distribution License (the License). 
> You may not use this file except in compliance with the License.  
>   
> You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the specific language governing 
> permission and limitations under the License.  
>  
> When distributing Covered Software, include this CDDL Header Notice in each file and include the License file at 
> legal/CDDLv1.0.txt. If applicable, add the following below the CDDL Header, with the fields enclosed by brackets [] 
> replaced by your own identifying information: "Portions copyright [year] [name of copyright owner]".  
>   

## Acknowledgments

* Sun Microsystems.
* ForgeRock AS.

