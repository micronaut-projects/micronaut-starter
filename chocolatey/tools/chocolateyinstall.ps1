$version = '3.8.11'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '7F3D887C3C5EA30C29A39B018F3BE1EA69DF460C6BD1B5B4FD3B1E2A6DA27932'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
