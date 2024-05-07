#!/usr/bin/env bash

getGitSha() {
  git rev-parse --short HEAD
}

getLastCommitMessages() {
  git log --pretty=format:'* %h %s' -n 5
}

getLastCommitDateTime() {
  git log --date=format:'Date: %d/%m/%y, Time: %H:%M:%S' --format=%cd -n1
}

releaseNotes() {
  printf "### Git hash 👾: \n%s \n\n### Latest commits in this build 🧑🏾‍💻: \n%s \n\n#### Last commit date 📆 :\n%s " "$(getGitSha)" "$(getLastCommitMessages)" "$(getLastCommitDateTime)" > release_notes_markdown.md
}

case "$1" in
  "") ;;
  releaseNotes) "$@"; exit;;
  *) log_error "Unknown function: $1()"; exit 2;;
esac