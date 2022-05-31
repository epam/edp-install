#!/usr/bin/env bash

set -ex

export LANG=en_US.UTF-8

helm plugin list

helmfile --log-level=debug  --environment ci -f ../helmfile.yaml lint