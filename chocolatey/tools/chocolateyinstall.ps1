$version = '4.0.0-M4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'FA5B3C997F23FFBB210C9B39195344D07BE98411414667D4BA490B1C873F0AC9'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
