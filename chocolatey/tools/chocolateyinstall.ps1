$version = '3.3.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D0ADD539322C55C6BD7A8B871D4907E65C172F5E2B70E0C75E5E55288B5E2FBC'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
