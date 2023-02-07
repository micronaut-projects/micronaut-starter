$version = '3.7.7'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '8AD32803694C3A92E787B8CE04028493376C4A5CC16C735E664FC1C6C5FE0384'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
