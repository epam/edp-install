import com.epam.edp.stages.impl.cd.Stage
import org.apache.commons.lang.RandomStringUtils

@Stage(name = "security-grants")
class SecurityGrants {
    Script script

    void run(context) {
        script.sh("oc adm policy add-scc-to-user edp -z edp -n ${context.job.deployProject}")
        script.sh("oc adm policy add-cluster-role-to-user edp-deploy-role -z edp " +
                "-n ${context.job.deployProject} --rolebinding-name=${context.job.deployProject}")
    }
}

return SecurityGrants
