$version = '5.1.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '99082458CDEDA7E8352E2993B4EFB529FC9B27E7F9FF13711EE34402FFF6B31C'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
