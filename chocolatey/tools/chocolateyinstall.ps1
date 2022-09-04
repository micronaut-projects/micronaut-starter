$version = '3.5.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '4D288898767B642409D427DF2097098590DE4351F057750B01B1C8B3873F0918'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
