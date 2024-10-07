$version = '4.6.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D0379C1B6710DCDF90CE02E9CFF231C81F4BD5B970C3F2B3BABB781819C76E4A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
