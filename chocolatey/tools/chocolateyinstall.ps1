$version = '4.0.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8E3B172F3D76BC2407E8C0AF8FCE49E93AAF87EA9A9FCBF31EE42E19265A6557'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
