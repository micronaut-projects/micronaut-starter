$version = '3.1.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'BF2673694B7CFB71CA5B306EB9BE8A0EDBB5D7D83E02819078F678B6B44E36DE'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
