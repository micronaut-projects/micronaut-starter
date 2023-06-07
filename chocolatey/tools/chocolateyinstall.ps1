$version = '3.9.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '67E1D83280EA285C1F3865DB4CD10187D1DB67081B847FC309EF28997CBC6798'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
