$version = '4.0.0-RC1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '965C280547FEF916AFF7BF29F3AF11D509B50916092F4401709060DE5558218D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
