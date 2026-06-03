$version = '5.0.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B3637F016A686E0E77697CAF9704D724CAC10855C7710DD88E95DFEF11BAA520'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
