$version = '4.8.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D69CF6E49875FF3EBA9AC8DA8440FF27DEB216ED5260A7FEADF7EE748894B82B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
