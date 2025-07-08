$version = '4.9.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8C73F1F244D7C560C07E4559168C9516F01B126D654B60BED56165F409F04FEC'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
