$version = '5.2.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'FD0D86C80D0E68581C692EE847D522C1744D01FAE0329C68F9B446149AB437EE'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
