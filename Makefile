.PHONY: help docs build-docs changelog

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
changelog:
	@git-chglog -o CHANGELOG.md v2.7.0..
