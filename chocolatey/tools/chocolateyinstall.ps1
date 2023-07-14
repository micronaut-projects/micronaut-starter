$version = '4.0.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '041F505B0356728E26870277B91620CCDE895F4EA327BD2A7EADD0BDA64346A2'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
