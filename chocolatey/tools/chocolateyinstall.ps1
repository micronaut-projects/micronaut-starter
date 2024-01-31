$version = '4.2.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'CD233206FF7991A2D45E3064729C919FE6EF8C628645D4EB20053B1A1398C6EA'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
