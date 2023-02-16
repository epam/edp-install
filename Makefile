.PHONY: help docs build-docs changelog

CURRENT_DIR=$(shell pwd)

help:
	@echo "Run make docs or make build-docs"

## check documents http://localhost:8000
docs: edp-docs-image
	@docker run --rm -it \
		-p 8000:8000 \
		-v ${PWD}:/docs \
		--entrypoint mkdocs \
		edp-docs serve --dev-addr=0.0.0.0:8000

build-docs: edp-docs-image
	@docker run --rm -it \
		-v ${PWD}:/docs \
		--entrypoint mkdocs \
		edp-docs build

edp-docs-image:
	@docker build -t edp-docs hack/mkdocs

# use https://github.com/git-chglog/git-chglog/
.PHONY: changelog
changelog: git-chglog	## generate changelog
ifneq (${NEXT_RELEASE_TAG},)
	$(GITCHGLOG) --next-tag v${NEXT_RELEASE_TAG} -o CHANGELOG.md v2.7.0..
else
	$(GITCHGLOG) -o CHANGELOG.md v2.7.0..
endif

.PHONY: validate-docs
validate-docs: helm-docs  ## Validate helm docs
	@git diff -s --exit-code deploy-templates/README.md || (echo "Run 'make helm-docs' to address the issue." && git diff && exit 1)

.PHONY: helm-docs
helm-docs: helmdocs	## generate helm docs
	$(HELMDOCS)

HELMDOCS = ${CURRENT_DIR}/bin/helm-docs
.PHONY: helmdocs
helmdocs: ## Download helm-docs locally if necessary.
	$(call go-get-tool,$(HELMDOCS),github.com/norwoodj/helm-docs/cmd/helm-docs,v1.10.0)

GITCHGLOG = ${CURRENT_DIR}/bin/git-chglog
.PHONY: git-chglog
git-chglog: ## Download git-chglog locally if necessary.
	$(call go-get-tool,$(GITCHGLOG),github.com/git-chglog/git-chglog/cmd/git-chglog,v0.15.4)

PROJECT_DIR := $(shell dirname $(abspath $(lastword $(MAKEFILE_LIST))))
define go-get-tool
@[ -f $(1) ] || { \
set -e ;\
TMP_DIR=$$(mktemp -d) ;\
cd $$TMP_DIR ;\
go mod init tmp ;\
echo "Downloading $(2)" ;\
go get -d $(2)@$(3) ;\
GOBIN=$(PROJECT_DIR)/bin go install $(2) ;\
rm -rf $$TMP_DIR ;\
}
endef
