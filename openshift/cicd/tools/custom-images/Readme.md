# How to push custom images to Openshift docker registry

## Prerequisites

- Please, ensure that you have exposed **docker-registry service** using **NodePort**. Also prepare the 5-sign port's value.
- Please, ensure that you have some project, if there is no such one, you can create it like this
 ```
 oc new-project ci-cd
 ```

## The process of installing and configuring Nexus and Jenkins

1. Switch to your project
```
oc project ci-cd
```
2. Create special account to push,
```
oc create serviceaccount pusher
```
3. Bind role with pushing rights to the account
```
oc policy add-role-to-user system:image-builder system:serviceaccount:**cd-cd**:pusher
```
4. Investigate the created account concerning its secrets
```
oc describe sa pusher
```
5. Retrieve token form one of the secrets
```
oc describe secret pusher-token-0fc4n
```
say, you got it and then you will save it to var
```
token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJjaS1jZCIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJwdXNoZXItdG9rZW4tOG16djkiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC5uYW1lIjoicHVzaGVyIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiYmU2NjBhY2EtZjVkNi0xMWU3LWI4YTAtMDA1MDU2OGU2MjhjIiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OmNpLWNkOnB1c2hlciJ9.A0kr8y_C-mUDybKo6m75aySRRtK6gGRWe2sVytcsn0nu5CL6jHoDkHevIw5b9zTFb2XSYjaXzCyeiDmN9kdR2uR2YIBvHM9qFHGF15RbLCQVj5Jf-0hN8hIfasGfMRmdJezLac4kNJQyTvTjvhITo2UGN2Ti65ivvCOUB1vRyATaRvV3PhyLI4fHry7Jh70k4GjmJgRxvq-JyVGkBoPgh2HRl6efZcC-8sfiGLuiY1mBHcpxh2ZOnI_Gu2gVq6veE8BB6_mooc-3kzMDKftnvpwEYmT-6aO3uypld_InwMAd6tbCOTJf-U4UxvyxLnaiZ0QxU4ICbrRs-ZydGAmnQg
```
6. after revising your cluster URL and prepared port of docker registry, you can login to docker registry like this
```
docker login -u pusher -p $token ecsd00100e91.epam.com:32021
```
If login succeeded, you can try to push your images to the registry.
7. Create an ImageStream object in OpenShift in your current project
```
   oc create -f - <<API

   apiVersion: v1

   kind: ImageStream

   metadata:

     annotations:

       description: Keeps track of changes in the application image

     name: myimage

   API
```
8. For pushing custom images you need to know a correct format
 ```
<registry>/<project>/<imagestream>:<tag>
```
Finally, you need to tag your custom image and push it
```
docker tag 445f4be0f46f ecsd00100e91.epam.com:32021/**ci-cd**/myimage:image-version
```
9. Check out that all is fine
```
oc describe is myimage
```

See more details [here](https://blog.openshift.com/remotely-push-pull-container-images-openshift/).