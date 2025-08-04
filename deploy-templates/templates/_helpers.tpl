{{/*
Expand the name of the chart.
*/}}
{{- define "edp-install.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "edp-install.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "edp-install.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "edp-install.labels" -}}
helm.sh/chart: {{ include "edp-install.chart" . }}
{{ include "edp-install.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "edp-install.selectorLabels" -}}
app.kubernetes.io/name: {{ include "edp-install.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "edp-install.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "edp-install.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Create the name of the secretstore to use
*/}}
{{- define "edp-install.secretStoreName" -}}
  {{- if .Values.externalSecrets.enabled }}
    {{- if eq .Values.externalSecrets.type "aws" }}
      {{- printf "%s-%s" "aws" .Values.externalSecrets.secretProvider.aws.service | lower }}
    {{- end }}
    {{- if eq .Values.externalSecrets.type "generic" }}
      {{- $secretStoreName := required "Secret store name is not defined" .Values.externalSecrets.secretProvider.generic.secretStore.name | lower }}
      {{- printf "%s" $secretStoreName }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Return the appropriate apiVersion for External Secrets
*/}}
{{- define "app.externalSecrets.apiVersion" -}}
  {{- if .Capabilities.APIVersions.Has "external-secrets.io/v1" -}}
    {{- print "external-secrets.io/v1" -}}
  {{- else if .Capabilities.APIVersions.Has "external-secrets.io/v1beta1" -}}
    {{- print "external-secrets.io/v1beta1" -}}
  {{- else -}}
    {{- print "external-secrets.io/v1beta1" -}}
  {{- end -}}
{{- end -}}
