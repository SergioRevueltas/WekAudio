@echo off
SETLOCAL ENABLEDELAYEDEXPANSION
SET old=)
SET new=\"
for /f "tokens=*" %%f in ('dir /b *.wav') do (
  SET newname=%%f
  SET newname=!newname:%old%=%new%!
  move "%%f" "!newname!"
)