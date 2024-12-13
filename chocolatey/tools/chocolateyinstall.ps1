$version = '4.7.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D9887CB6D50EFBAFECC374F6E088E355E3A8C921B705757F07EDDA3F9E89F025'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
