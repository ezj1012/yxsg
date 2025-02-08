package com.yxbear.core.coder.gen;

import java.util.Arrays;
import java.util.List;

import com.yxbear.core.coder.CodeBuilder;
import com.yxbear.core.coder.CodeBuilder.AttrBuilder;
import com.yxbear.core.coder.CodeBuilder.ModelBuilder;
import com.yxbear.core.coder.CodeBuilder.PackageBuilder;
import com.yxbear.core.coder.configuration.ProjectProps;

public class GenModelGen {

    ProjectProps props;

    public GenModelGen(ProjectProps props) {
        super();
        this.props = props;
    }

    public void gen(GenModel cfg) {
        gen(cfg.getPkg(), Arrays.asList(cfg));
    }

    private void gen(String pkg, List<GenModel> cfgs) {
        if (cfgs == null || cfgs.isEmpty()) { return; }
        PackageBuilder pkgB = getBuilder().pkg(pkg);

        for (GenModel gen : cfgs) {
            ModelBuilder model = pkgB.model(gen.getModelName(), gen.extendsType);
            gen.getAttrs().forEach(attr -> {
                model.attr(AttrBuilder.parse(model, attr));
            });
            model._____endCheck____();
        }

        //
        pkgB.buildPackageCode();
    }

    public CodeBuilder getBuilder() {
        return new CodeBuilder(this.props.getName(), this.props.getJavaPackage());
    }

}
