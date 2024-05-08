$version = '4.4.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '01ED97D7467E353A38270C79037CF09006B263073DAE3D2E9FDBE92F9D510D80'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
