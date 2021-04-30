$version = '2.5.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '0EC9C949F3E29AC0896AC55E2D9C783A8A57E92FEF37DF27E8A1B4D7322788F6'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
