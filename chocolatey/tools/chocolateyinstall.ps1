$version = '3.6.6'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '7702BCFA1333AD9A6F27CCFDDFC4EC9AEC16D6F36BAE0C6FD2C95BEC1A575B2D'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
